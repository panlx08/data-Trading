package hfut.hu.BlockValueShare.blockbean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import hfut.hu.BlockValueShare.common.StringUtil;
public class SideBlock {
	/**
	 * 
	 *
	 * @author 
	 */
	    public String hash;
	    public String previousHash;
	    public String merkleRoot;
	    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
	    public long timeStamp; //as number of milliseconds since 1/1/1970.
	    public int nonce;

	    public SideBlock() {
	    	 this.timeStamp = new Date().getTime();
	    }
	    
	    public SideBlock(String previousHash) {
	        this.previousHash = previousHash;
	        this.timeStamp = new Date().getTime();
	        this.hash = calculateHash();
	    }

	    @Override
		public String toString() {
			return "SideBlock [hash=" + hash + ", previousHash=" + previousHash + ", merkleRoot=" + merkleRoot
					+ ", transactions=" + transactions + ", timeStamp=" + timeStamp + ", nonce=" + nonce + "]";
		}

		//根据块内容计算新的散列
	    public String calculateHash() {
	        String calculatedhash = StringUtil.applySha256(
	                previousHash +
	                        Long.toString(timeStamp) +
	                        Integer.toString(nonce) +
	                        merkleRoot
	        );
	        return calculatedhash;
	    }

	    //增加nonce值，直到到达散列目标。
	    public void mineBlock(int difficulty) {
	        merkleRoot = StringUtil.getMerkleRoot2(transactions);

	        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
	        while (!hash.substring(0, difficulty).equals(target)) {
	            nonce++;
	            hash = calculateHash();
	        }
	        System.out.println("Block Mined!!! : " + hash);
	    }

	    //将事务添加到此块
	    public boolean addTransaction(HashMap<String,TransactionOutput> UTXOs,Transaction transaction) throws Exception {
	        //处理事务并检查是否有效，除非块是genesis块，否则忽略。
	        if (transaction == null) return false;
	        if ((previousHash != "0")) {
	            if ((transaction.processTransaction(UTXOs) != true)) {
	                System.out.println("Transaction failed to process. Discarded.");
	                return false;
	            }
	        } 
//	        System.out.println("222333");
//	        System.out.println("222333"+transaction);
	        transactions.add(transaction);
	        System.out.println("Transaction Successfully added to Block");
	        return true;
	    }
		public String getHash() {
			return hash;
		}
		public void setHash(String hash) {
			this.hash = hash;
		}
		public String getPreviousHash() {
			return previousHash;
		}
		public void setPreviousHash(String previousHash) {
			this.previousHash = previousHash;
		}
		public String getMerkleRoot() {
			return merkleRoot;
		}
		public void setMerkleRoot(String merkleRoot) {
			this.merkleRoot = merkleRoot;
		}
		public ArrayList<Transaction> getTransactions() {
			return transactions;
		}
		public void setTransactions(ArrayList<Transaction> transactions) {
			this.transactions = transactions;
		}
		public long getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		public int getNonce() {
			return nonce;
		}
		public void setNonce(int nonce) {
			this.nonce = nonce;
		}
	
	    
}
