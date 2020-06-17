package hfut.hu.BlockValueShare.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.blockbean.TradeBlock;
import hfut.hu.BlockValueShare.blockbean.Transaction;
import hfut.hu.BlockValueShare.blockbean.TransactionInput;
import hfut.hu.BlockValueShare.blockbean.TransactionOutput;
import hfut.hu.BlockValueShare.blockbean.Wallet;


public class TradeChain {

	/**
	 **存储交易信息的侧链	
	 */
	private static List<SideBlock> sideChain = new ArrayList<SideBlock>();
//	/**
//	 * 存放交易
//	 */
//	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	public static Transaction genesisTransaction;
	public static int difficulty = 3;
    public static float minimumTransaction = 0.1f;
    final Gson gson = new GsonBuilder().create();
	final static Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    
	public TradeChain(List<SideBlock> sideChain) {
        // 初始化区块链
    	setSideChain(sideChain);	
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
    /**
     **交易链存储一定量交易后,在链上生成区块
     */
    public static void addBlock(SideBlock newBlock) {
        newBlock.mineBlock(difficulty);
        getSideChain().add(newBlock);
    }
	/**
	 * 校验区块的合法性（有效性）
	 * 
	 * @param newBlock
	 * @param oldBlock
	 * @return
	 * @throws Exception 
	 */
    /*
     **判断交易链合法
     */
    public static Boolean isChainValid() throws Exception {
        SideBlock currentBlock;
        SideBlock previousBlock;
        
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
      //loop through blockchain to check hashes:
        for(int i=1; i < getSideChain().size(); i++) {

            currentBlock = getSideChain().get(i);
            previousBlock = getSideChain().get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

	/**
	 * 如果有别的链比你长，就用比你长的链作为区块链
	 * 
	 * @param oldBlocks
	 * @param newBlocks
	 * @return 结果链
	 */
	public List<Block> replaceChain(List<Block> oldBlocks,List<Block> newBlocks) {
		if (newBlocks.size() > oldBlocks.size()) {
			return newBlocks;
		}else{
			return oldBlocks;
		}
	}
	public List<SideBlock> getChain() {
		return getSideChain();
	}
	public void setChain(List<SideBlock> sidechain) {
		TradeChain.setSideChain(sidechain);
	}
    /**
     * @return 得到区块链中的最后一个区块
     */
    public SideBlock lastBlock() {
        return getChain().get(getChain().size() - 1);
    }
	public static List<SideBlock> getSideChain() {
		return sideChain;
	}
	public static void setSideChain(List<SideBlock> sideChain) {
		TradeChain.sideChain = sideChain;
	}
    
}
