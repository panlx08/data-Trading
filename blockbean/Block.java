package hfut.hu.BlockValueShare.blockbean;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
/**
 *Block   bean
 *	区块
 *@author
 */
public class Block {
	/* 
	 * 	区块头
	 */
	private BlockHeader blockHeader;
	/* 
	 * 	区块body
	 */
	private BlockBody blockBody;
	/* 
	 * 	区块的哈希
	 */
	public String blockHash;
	/*
	 * 	区块地址
	 */
	private String ip;
	/*
	 **每个区块存储一个自己的评价链
	 */
	private List<EvaluateBlock> evaluateChain;
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void addEva(EvaluateBlock evaluateBlock) {
		evaluateChain.add(evaluateBlock);
	}
	/**
	 * 
	 * @return 返回评价链最后一块
	 */
	public EvaluateBlock lastEva() {
		int last = evaluateChain.size()-1;
		EvaluateBlock lastBlock = evaluateChain.get(last);
		return lastBlock;
	}
	
	
	
	@Override
	public String toString() {
		return "Block [blockHeader=" + blockHeader + ", blockBody=" + blockBody + ", blockHash=" + blockHash
				+  "]";
	}

	
	public static int difficulty = 5;
	public Block() throws Exception {
		BlockHeader blockHeader = new BlockHeader();
		BlockBody blockBody = new BlockBody();
		InetAddress addr = InetAddress.getLocalHost();
		String ip1 = addr.getHostAddress();
		evaluateChain = new LinkedList<EvaluateBlock>();
		this.ip = ip1;
	    this.blockHeader = blockHeader;
	    this.blockBody = blockBody;
	}

	public BlockHeader getBlockHeader() {
		return blockHeader;
	}
	public void setBlockHeader(BlockHeader blockHeader) {
		this.blockHeader = blockHeader;
	}
	public BlockBody getBlockBody() {
		return blockBody;
	}
	public void setBlockBody(BlockBody blockBody) {
		this.blockBody = blockBody;
	}
	
	public String getBlockHash() {
		return blockHash;
				
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public List<EvaluateBlock> getEvaluateChain() {
		return evaluateChain;
	}

	public void setEvaluateChain(List<EvaluateBlock> evaluateChain) {
		this.evaluateChain = evaluateChain;
	}

	
	
	}
