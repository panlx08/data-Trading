package hfut.hu.BlockValueShare.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hfut.hu.BlockValueShare.blockbean.TransactionOutput;
import hfut.hu.BlockValueShare.blockbean.Wallet;
import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.blockbean.ChaZhaoBean;
import hfut.hu.BlockValueShare.blockbean.ContentInfo;
import hfut.hu.BlockValueShare.blockbean.EvaluateBlock;
import hfut.hu.BlockValueShare.blockbean.FenShu;
import hfut.hu.BlockValueShare.blockbean.PingJiaBean;
import hfut.hu.BlockValueShare.blockbean.QianBao;
import hfut.hu.BlockValueShare.blockbean.ShangChuan;
import hfut.hu.BlockValueShare.blockbean.SideBlock;
import hfut.hu.BlockValueShare.blockbean.Transaction;
import hfut.hu.BlockValueShare.blockchain.BlockChain;
import hfut.hu.BlockValueShare.blockchain.EvaluateChain;
import hfut.hu.BlockValueShare.pbft.HandPbft;
import hfut.hu.BlockValueShare.pbft.VoteType;
import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;
import hfut.hu.BlockValueShare.pbft.queue.PrepareMsgQueue;
import hfut.hu.BlockValueShare.util.IpUtils;
import spark.utils.StringUtils;
/**
 * 节点
 * 
 * @author Mignet
 *
 */
public class Node  {
	private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);
	/** 本地存储的区块链 */
	private static List<Block> blockChain = new LinkedList<Block>();
	private static List<SideBlock> sideChain = new LinkedList<SideBlock>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	public static List<VotePreMsg> prepre = new LinkedList<VotePreMsg>();
	public static HashMap<String,VotePreMsg> pre = new HashMap<String,VotePreMsg>();
	public static HashMap<String,VotePreMsg> com = new HashMap<String,VotePreMsg>();
	private static final String VERSION = "0.1";
	private static int PBFTSize = 3;
	public static QianBao A;
	/**
     **交易链存储一定量交易后,在链上生成区块
     */
	public static void addBlock(SideBlock newBlock) {
        newBlock.mineBlock(3);
        sideChain.add(newBlock);
    }
	/**
	 * 生成交易区块
	 * 当期链 交易集合，来源钱包，目的公钥，钱
	 */
    public static SideBlock generateBlock(HashMap<String,TransactionOutput> UTXOs,Wallet walletA, String to,float value) throws Exception {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());    	
        SideBlock newBlock = new SideBlock(sideChain.get(sideChain.size()-1).hash);
        newBlock.addTransaction(UTXOs,walletA.sendFunds(UTXOs,to, value));
		return newBlock;
    }
    public static SideBlock generateBlock(HashMap<String,TransactionOutput> UTXOs,QianBao walletA, String to,float value) throws Exception {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());    	
        SideBlock newBlock = new SideBlock(sideChain.get(sideChain.size()-1).hash);
        newBlock.addTransaction(UTXOs,walletA.sendFunds(UTXOs,to, value));
		return newBlock;
    }
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
//		/** 生成公钥,用AES加密 */
//		KeySuitVO keySuitVO =new KeySuitVO();
//		String secretKey = keySuitVO.getSymmetricKey(); //本节点公钥
		final Gson gson = new GsonBuilder().create();
		final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		int port = 8015;
		LOGGER.info("Starting peer network...  ");
		PeerNetwork peerNetwork = new PeerNetwork(port);
		peerNetwork.start();

		LOGGER.info("[  Node is Started in port:"+port+"  ]");
		
		LOGGER.info("Starting RPC daemon...  ");
		RpcServer rpcAgent = new RpcServer(port+1);
		rpcAgent.start();
		LOGGER.info("[  RPC agent is Started in port:"+(port+1)+"  ]");
		
		LOGGER.info("");		
		ArrayList<String> peers = new ArrayList<String>();
		File peerFile = new File("peers.list");
		if (!peerFile.exists()) {
			String host = InetAddress.getLocalHost().getHostAddress().toString();
			FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
		}else{
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {
				String[] addr = peer.split(":");
				if(IpUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					continue;
				}
				peers.add(peer);
				//raw ipv4
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
		}
		File sideFile = new File("sideBlock.bin");
		File pubKey = new File("publicKey");
		File priKey = new File("privateKey");
		File UTOFile = new File("UTXOs.obj");
		
		if (!sideFile.exists() || !pubKey.exists() || !priKey.exists() || !UTOFile.exists()) {
			//创世交易区块
	        Wallet coinbase = new Wallet();
	        Wallet walletA = new Wallet();
	        A = new QianBao(walletA.publicKey, walletA.privateKey);
	        Transaction transac;
	        transac = new Transaction(coinbase.publicKey, walletA.publicKey, 1000f, null);
	        transac.generateSignature(coinbase.privateKey);	 //手工签署genesis事务
	        transac.transactionId = "0"; //手动设置事务id
	        transac.outputs.add(new TransactionOutput(transac.reciepient, transac.value, transac.transactionId)); //手动添加事务输出
	        UTXOs.put(transac.outputs.get(0).id, transac.outputs.get(0));
	        SideBlock genesis = new SideBlock("0");
	        genesis.addTransaction(UTXOs,transac);
	        addBlock(genesis);	        
	        System.out.println("\nWalletA's balance is: " + walletA.getBalance(UTXOs));
	        //写入本地文件
	        FileUtils.writeStringToFile(pubKey,walletA.publicKey, StandardCharsets.UTF_8,false);
	        FileUtils.writeStringToFile(priKey,walletA.privateKey, StandardCharsets.UTF_8,false);
			FileUtils.writeStringToFile(sideFile,gson.toJson(genesis), StandardCharsets.UTF_8,true);
			FileOutputStream fos = new FileOutputStream(UTOFile);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(UTXOs);
	        oos.flush();
	        oos.close();
		}else{
			//读
			FileInputStream fis = new FileInputStream(UTOFile);
	        ObjectInputStream ois = new ObjectInputStream(fis);         
	        HashMap<String, TransactionOutput> utxoRead = new HashMap<String, TransactionOutput>();
	        utxoRead = (HashMap<String, TransactionOutput>) ois.readObject();
	        ois.close();
	        for (Map.Entry<String, TransactionOutput> item : utxoRead.entrySet()) {
	               UTXOs.put(item.getKey(), item.getValue());
	           }
			List<String> r = FileUtils.readLines(priKey, StandardCharsets.UTF_8);
			String privateKey = r.get(0);
			List<String> u = FileUtils.readLines(pubKey, StandardCharsets.UTF_8);
			String publicKey = u.get(0);
			A = new QianBao(publicKey, privateKey);
			
			List<String> list = FileUtils.readLines(sideFile, StandardCharsets.UTF_8);
			for(String line:list){
				sideChain.add(gson.fromJson(line, SideBlock.class));
			}
			System.out.println("\nWallet's balance is: " + A.getBalance(UTXOs));
		}
		
		File dataFile = new File("block.bin");	
		if (!dataFile.exists()) {
			//创世区块
			FileUtils.writeStringToFile(dataFile,gson.toJson(genesisBlock()), StandardCharsets.UTF_8,true);
			blockChain.add(genesisBlock());
		}else{
			List<String> list = FileUtils.readLines(dataFile, StandardCharsets.UTF_8);
			for(String line:list){
				blockChain.add(gson.fromJson(line, Block.class));
			}
		}
		//pretty print
		LOGGER.info(prettyGson.toJson(blockChain));
		LOGGER.info(prettyGson.toJson(sideChain));
		int bestHeight = blockChain.size();
//		System.out.println(bestHeight);
		//建立socket连接后，给大家广播握手
		peerNetwork.broadcast("VERSION "+ bestHeight+" " + VERSION);
		
		/**
		 * p2p 通讯
		 */
		while (true) {
			//对新连接过的peer写入文件，下次启动直接连接
			for (String peer : peerNetwork.peers) {
				if (!peers.contains(peer)) {
					peers.add(peer);
					LOGGER.info("add peer to file:"+peer);
					FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
				}
			}
			peerNetwork.peers.clear();

			// 处理通讯
			for (PeerThread pt : peerNetwork.peerThreads) {
				if (pt == null || pt.peerReader == null) {
					break;
				}
				List<String> dataList = pt.peerReader.readData();
				if (dataList == null) {
					LOGGER.info("Null return, retry.");
					System.exit(-5);
					break;
				}
				for (String data:dataList) {
					LOGGER.info("[p2p] COMMAND:: " + data);
					int flag = data.indexOf(' ');
					String cmd = flag >= 0 ? data.substring(0, flag) : data;
					String payload = flag >= 0 ? data.substring(flag + 1) : "";
					if (StringUtils.isNotBlank(cmd)) {
						if ("VERACK".equalsIgnoreCase(cmd)) {
							// 对方确认知道了,并给我区块高度
							String[] parts = payload.split(" ");
							bestHeight = Integer.parseInt(parts[0]);
							//哈希暂时不校验
						} else if ("VERSION".equalsIgnoreCase(cmd)) {
							// 对方发来握手信息
							// 获取区块高度和版本号信息
							String[] parts = payload.split(" ");
							bestHeight = Integer.parseInt(parts[0]);
							//我方回复：知道了
							pt.peerWriter.write("VERACK " + blockChain.size() + " " + blockChain.get(blockChain.size() - 1).getBlockHash());
						} else if ("BLOCK".equalsIgnoreCase(cmd)) {
							//把对方给的块存进链中
							Block newBlock = gson.fromJson(payload, Block.class);
							if (!blockChain.contains(newBlock)) {
								LOGGER.info("Attempting to add Block: " + payload);
								// 校验区块，如果成功，将其写入本地区块链
								if (BlockChain.isBlockValid(newBlock, blockChain.get(blockChain.size() - 1))) {
									blockChain.add(newBlock);
									LOGGER.info("Added block " + newBlock.getBlockHeader().getIndex() + " with hash: ["+ newBlock.getBlockHash() + "]");
									FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(newBlock), StandardCharsets.UTF_8,true);
									peerNetwork.broadcast("BLOCK " + payload);
								}
							}
						} else if ("GET_BLOCK".equalsIgnoreCase(cmd)) {
							//把对方请求的块给对方
							Block block = blockChain.get(Integer.parseInt(payload));
							if (block != null) {
								LOGGER.info("Sending block " + payload + " to peer");
								pt.peerWriter.write("BLOCK " + gson.toJson(block));
							}
						} else if ("ADDR".equalsIgnoreCase(cmd)) {
							// 对方发来地址，建立连接并保存
							if (!peers.contains(payload)) {
								String peerAddr = payload.substring(0, payload.indexOf(':'));
								int peerPort = Integer.parseInt(payload.substring(payload.indexOf(':') + 1));
								peerNetwork.connect(peerAddr, peerPort);
								peers.add(payload);
								PrintWriter out = new PrintWriter(peerFile);
								for (int k = 0; k < peers.size(); k++) {
									out.println(peers.get(k));
								}
								out.close();
							}
						} else if ("GET_ADDR".equalsIgnoreCase(cmd)) {
							//对方请求更多peer地址，随机给一个
							Random random = new Random();
							pt.peerWriter.write("ADDR " + peers.get(random.nextInt(peers.size())));
						} else if ("PBFT".equalsIgnoreCase(cmd)) {
							//如果收到投票信息，进行投票处理
							LOGGER.info("receive pbftVote");
							VotePreMsg votemsg = gson.fromJson(payload, VotePreMsg.class);
							if (prepre.isEmpty()) {
								prepre.add(votemsg);									
							}else {
								prepre.set(0, votemsg);	
							} 
							votemsg.setVoteType(VoteType.PREPARE);
							votemsg.setAppId(PrepareMsgQueue.IP());
							peerNetwork.broadcast("PREP "+gson.toJson(votemsg));							
						}else if ("PREP".equalsIgnoreCase(cmd)) {
							//如果收到投票信息，进行投票处理
							LOGGER.info("prep pbftVote");
							VotePreMsg votemsg = gson.fromJson(payload, VotePreMsg.class);
							//2f+1
							pre.put(votemsg.getAppId(),votemsg);
							if (pre.size() >= PBFTSize) {
								votemsg.setVoteType(VoteType.PREPARE);
								votemsg.setAppId(PrepareMsgQueue.IP());
								votemsg.setAgree(true);
								peerNetwork.broadcast("COMMIT " + gson.toJson(votemsg));
							}	
							
						}
						else if ("COMMIT".equalsIgnoreCase(cmd)) {
							LOGGER.info("receive commitVote");
							VotePreMsg votemsg = gson.fromJson(payload, VotePreMsg.class);
							//2f+1
							com.put(votemsg.getAppId(),votemsg);
							if (com.size() >= PBFTSize) {							
								blockChain.add(votemsg.getBlock());
								peerNetwork.broadcast("DONE "+gson.toJson(votemsg));	
								FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(votemsg.getBlock()), StandardCharsets.UTF_8,true);
							}
						}else if ("DONE".equalsIgnoreCase(cmd)) {
							LOGGER.info("receive commitVote");
							VotePreMsg votemsg = gson.fromJson(payload, VotePreMsg.class);
							if (! blockChain.get(blockChain.size()-1).blockHash
									.equals(votemsg.getBlock().blockHash)) {
								blockChain.add(votemsg.getBlock());
								FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(votemsg.getBlock()), StandardCharsets.UTF_8,true);
							}
						}
					}
				}
			}

			// ********************************
			// 		比较区块高度,同步区块
			// ********************************
			int localHeight = blockChain.size();
			if (bestHeight > localHeight) {
				LOGGER.info("Local chain height: " + localHeight+" Best chain Height: " + bestHeight);
				TimeUnit.MILLISECONDS.sleep(300);
				
				for (int i = localHeight; i < bestHeight; i++) {
					LOGGER.info("request get block[" + i + "]...");
					peerNetwork.broadcast("GET_BLOCK " + i);
				}
			}

			// ********************************
			// 处理RPC服务
			// ********************************
			for (RpcThread th:rpcAgent.rpcThreads) {
				String request = th.req;
				if (request != null) {
					String[] parts = request.split(" ");
					parts[0] = parts[0].toLowerCase();
					if ("getinfo".equals(parts[0])) {
						String res = prettyGson.toJson(blockChain);
						th.res = res;
					}
					else if ("getinfo2".equals(parts[0])) {
						String res = prettyGson.toJson(sideChain);
						th.res = res;
					}
					//查询
					else if ("find".equals(parts[0])) {
						try {
							String find = parts[1];
							ChaZhaoBean bean = gson.fromJson(find, ChaZhaoBean.class);
							int a= bean.getNum();
							th.res = prettyGson.toJson(blockChain.get(a));			
							}
						 catch (Exception e) {
							th.res = "find error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					} 
					//查询
					else if ("finds".equals(parts[0])) {
						try {
							String find = parts[1];
							ChaZhaoBean bean = gson.fromJson(find, ChaZhaoBean.class);
							String a= bean.getPublicKey();
							int currentIndex= 0;
							ArrayList<Transaction> getList = new ArrayList<Transaction>();
							while(currentIndex < blockChain.size()) {
								int index = 0;
								if (sideChain.get(currentIndex).getTransactions().size()>0) {
									while (index < sideChain.get(currentIndex).getTransactions().size()) {						
										if (sideChain.get(currentIndex).getTransactions().get(0).getSender().equals(a)) {
												getList.add(sideChain.get(currentIndex).getTransactions().get(0));
											}
										else if (sideChain.get(currentIndex).getTransactions().get(0).getReciepient().equals(a)) {
											getList.add(sideChain.get(currentIndex).getTransactions().get(0));
										}
										index++;
									} 
								}
								currentIndex ++;
						}
							th.res = prettyGson.toJson(getList);			
							}
						 catch (Exception e) {
							th.res = "find error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					} 
					//广播
					else if ("send".equals(parts[0])) {
						try {
							String send = parts[1];
							ShangChuan bean = gson.fromJson(send, ShangChuan.class);
							// 根据vac创建区块
							Block newBlock = BlockChain.generateBlock(blockChain.get(blockChain.size() - 1), bean);
							if (BlockChain.isBlockValid(newBlock, blockChain.get(blockChain.size() - 1))) {
//								blockChain.add(newBlock);
//								th.res = "Block write Success!";
//								FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(newBlock), StandardCharsets.UTF_8,true);															
								VotePreMsg topMsg = HandPbft.topbft(newBlock);
								peerNetwork.broadcast("PBFT " + gson.toJson(topMsg));								
								prepre.add(topMsg);
								th.res = "Block writing";
							} else {
								th.res = "RPC 500: Invalid vac Error";
							}
						} catch (Exception e) {
							th.res = "send error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					}
					//创建
					else if ("sends".equals(parts[0])) {
						try {
							String send = parts[1];
							ShangChuan bean = gson.fromJson(send, ShangChuan.class);
							// 根据vac创建区块
							Block newBlock = BlockChain.generateBlock(blockChain.get(blockChain.size() - 1),bean);
							if (BlockChain.isBlockValid(newBlock, blockChain.get(blockChain.size() - 1))) {
								blockChain.add(newBlock);
								th.res = "Block Temporary write!"+ prettyGson.toJson(blockChain.get(blockChain.size()-1));
							} else {
								th.res = "RPC 500: Invalid vac Error";
							}
						} catch (Exception e) {
							th.res = "send error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					}
					else if ("mama".equals(parts[0])) {
						//data 类型 数量  
						try {
							String mama = parts[1];
							ChaZhaoBean bean = gson.fromJson(mama, ChaZhaoBean.class);
							String type = bean.getType(); //类型
							int num = bean.getNum(); //数量
							double newV;
							int currentIndex= 0;
							Map<String, FenShu> zhao = new TreeMap<String, FenShu>(
					                new Comparator<String>() {
					                    public int compare(String obj1, String obj2) {
					                        // 降序排序
					                    return obj2.compareTo(obj1);
					                   }
						               });
							FenShu shu = new FenShu();
							while(currentIndex < blockChain.size()) {
								int index = 0;
								if (blockChain.get(currentIndex).getBlockBody().getDataSet()
											.size()>0) {
									while (index < blockChain.get(currentIndex).getBlockBody().getDataSet()
											.size()) {						
										if (blockChain.get(currentIndex).getBlockHeader().getType().equals(type) && num>0) {
											//类型满足
											int score= EvaluateChain.GetScore(blockChain.get(currentIndex).getEvaluateChain());
											shu = new FenShu(currentIndex, blockChain.get(currentIndex).getBlockBody().getDataSet().get(index)
													.getNum(),blockChain.get(currentIndex).getIp());
											newV = FenShu.Normalization(currentIndex,0,blockChain.size());
											if (zhao.get(""+(score*newV)) != null) {
												if (zhao.get(""+(score*newV)).getNum()<= shu.getNum()) {
													zhao.put(""+(score*newV), shu);
												}
											}
											else {
												zhao.put(""+(score*newV), shu);
											}
										}
										index++;
									} 
								}
								currentIndex ++;
							}
							Set<String> keySet = zhao.keySet();
							Iterator<String> iter = keySet.iterator();
							int aaa =0;
							ArrayList<FenShu> data = new ArrayList<FenShu>();
							while (iter.hasNext()) {
									String key = iter.next();
									if (aaa > 2*num) {
										break;
									}
									else {
										aaa = aaa+ zhao.get(key).getNum();
										data.add(zhao.get(key));
									}
//							        System.out.println(key + ":" + zhao.get(key).getIndex());
							   }
//							if (aaa< num) {
//								th.res = gson.toJson(data);
//							}
							th.res = gson.toJson(data);
						} catch (Exception e) {
							th.res = "data error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}	
					}
//					//合约
//					else if ("mama".equals(parts[0])) {
//						//data 类型 数量  
//						try {
//							String mama = parts[1];
//							ChaZhaoBean bean = gson.fromJson(mama, ChaZhaoBean.class);
//							String type = bean.getType(); //类型
//							int num = bean.getNum(); //数量
//							int currentIndex= 0;
//							Map<String,String> getList = new HashMap<String, String>();
//							while(currentIndex < blockChain.size()) {
//								int index = 0;
//								if (blockChain.get(currentIndex).getBlockBody().getDataSet()
//											.size()>0) {
//									while (index < blockChain.get(currentIndex).getBlockBody().getDataSet()
//											.size()) {						
//										if (blockChain.get(currentIndex).getBlockHeader().getType().equals(type) && num>0) {
//											//类型满足
//											if (blockChain.get(currentIndex).getBlockBody().getDataSet().get(index)
//													.getNum() >= num) {						
//												getList.put(String.valueOf(blockChain.get(currentIndex).getBlockHeader().getIndex()),
//																String.valueOf(blockChain.get(currentIndex).getBlockBody().getDataSet().get(0).getNum())+"条 "+
//																		String.valueOf(EvaluateChain.GetPingjia(blockChain.get(currentIndex).getEvaluateChain())+"分"));
//												num = 0;
//											}
//											else if (blockChain.get(currentIndex).getBlockBody().getDataSet()
//													.get(index).getNum() < num) {
//												getList.put(String.valueOf(blockChain.get(currentIndex).getBlockHeader().getIndex()),
//														String.valueOf(blockChain.get(currentIndex).getBlockBody().getDataSet().get(0).getNum())+"条 "+
//																String.valueOf(EvaluateChain.GetPingjia(blockChain.get(currentIndex).getEvaluateChain())+"分"));
//												num= num -blockChain.get(currentIndex).
//														getBlockBody().getDataSet().get(index).getNum();
//											}
//										}
//										index++;
//									} 
//								}
//								currentIndex ++;
//						}
//							String i = null;
//							for (Entry<String, String> entry : getList.entrySet()) { 
//								
//								 i = "FROM: " + entry.getKey() + ", Score: " + entry.getValue()+"  " + i; 
//								}
//							th.res = i;
//						} catch (Exception e) {
//							th.res = "data error";
//							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
//						}	
//					}
					//评价
					else if ("baba".equals(parts[0])) {
						try {
							String baba = parts[1];
							PingJiaBean bean = gson.fromJson(baba, PingJiaBean.class);
							 int index=bean.getFind(); //高度	
							 Block oldBlock = blockChain.get(index);
							 
							 EvaluateBlock newEvaBlock = EvaluateChain.generateBlock(oldBlock);	
							 newEvaBlock.setEvaluate(bean.getEvaluate());
							 newEvaBlock.setScore(bean.getScore());
							 newEvaBlock.setEvaluate2(bean.getEvaluate2());
							 blockChain.get(index).addEva(newEvaBlock);
							 dataFile.delete();
							 FileUtils.writeStringToFile(dataFile,gson.toJson(genesisBlock()), StandardCharsets.UTF_8,true);
							 int currentIndex= 1;
							while(currentIndex < blockChain.size()) { //遍历每一块
								Block jjBlock  = blockChain.get(currentIndex);
								FileUtils.writeStringToFile(dataFile,"\r\n"+gson.toJson(jjBlock), StandardCharsets.UTF_8,true);
								currentIndex ++;
							}
							 th.res = "Evaluate write Success!";
						} catch (Exception e) {
							th.res = "send error";
							LOGGER.error("chucuo");
						}
					}
					//查询余额
					else if ("wallet".equals(parts[0])) {
						try {
							String baba = parts[1];
							Transaction bean = gson.fromJson(baba, Transaction.class);
							String sellPu = bean.getSender(); //失去积分
							String sellPr = bean.getSignature();
							QianBao bao = new QianBao(sellPu, sellPr);
					        th.res = "\nWallet's balance is: " + bao.getBalance(UTXOs);
						} catch (Exception e) {
							th.res = "wallet error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					}
					//交易
					else if ("tran".equals(parts[0])) {
						try {
							String baba = parts[1];
							Transaction bean = gson.fromJson(baba, Transaction.class);
							float bi = bean.getValue();
							String get = bean.getReciepient(); //得积分
							String sellPu = bean.getSender(); //失去积分
							String sellPr = bean.getSignature();
							A.privateKey = sellPr;
							A.publicKey = sellPu;
							System.out.println("sss"+A.privateKey);
					        SideBlock newBLock = generateBlock(UTXOs, A, get, bi);
					        addBlock(newBLock);
					        FileUtils.writeStringToFile(sideFile,"\r\n"+gson.toJson(newBLock), StandardCharsets.UTF_8,true);
					        FileOutputStream fos = new FileOutputStream(UTOFile);
					        ObjectOutputStream oos = new ObjectOutputStream(fos);
					        oos.writeObject(UTXOs);
					        oos.flush();
					        oos.close();
					        th.res = "\nWallet's balance is: " + A.getBalance(UTXOs);
						} catch (Exception e) {
							th.res = "wallet error";
							LOGGER.error("invalid vac - Virtual Asset Count(Integer)");
						}
					}
					else {
						th.res = "Unknown command: \"" + parts[0] + "\" ";
					}
				}
			}
			
			// ****************
			// 循环结束
			// ****************
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
	public static Block genesisBlock() throws Exception {
		Block genesisBlock = new Block();
		genesisBlock.getBlockHeader().setIndex(0);
		genesisBlock.getBlockHeader().setTimestamp("2019-04-17 14:43:00");
		genesisBlock.getBlockHeader().setHashPreviousBlock("GENESISBLOCK");
		genesisBlock.getBlockHeader().setType("genesis");
		List<String> contents = new ArrayList<String>();
		contents.add("欢迎使用数据交互系统");
		ContentInfo e = new ContentInfo(contents,"创世");
		genesisBlock.getBlockBody().getDataSet().add(e);	
		genesisBlock.setBlockHash(BlockChain.calculateHash(genesisBlock));	
		EvaluateBlock eGenesisBlock = new EvaluateBlock();	
		eGenesisBlock.setPreHash(genesisBlock.getBlockHash());
		eGenesisBlock.setEvaluate("欢迎使用");
		eGenesisBlock.setEvaluate2("使用愉快");
		genesisBlock.addEva(eGenesisBlock);
		genesisBlock.getBlockBody().getDataSet().get(0).setCost(1000);
		return genesisBlock;
	}
	
}
