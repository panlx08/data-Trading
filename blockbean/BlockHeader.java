package hfut.hu.BlockValueShare.blockbean;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * #区块头
 * @author
 */
public class BlockHeader{
	/* 
	 * 	版本号
	 */
	private String version ="1.0.0";
	/* 
	 * 	上一块哈希
	 */
	private String preBlockHash;
	/* 
	 * 	merkel树根节点哈希
	 */
	private String hashMerkleRoot;
	/* 
	 * 	生成该区块的公钥
	 */
	private String publicKey;
	/* 
	 * 	区块的序号
	 */
	private int index;
	/* 
	 * 	时间戳
	 */
	private String timeStamp;
	/* 
	 * 	该数据的类型
	 */
	private String type;
	
	public BlockHeader() {
		this.hashMerkleRoot= getMerkleRoot();
		this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	public BlockHeader(String hashPreviousBlock) {
		this.hashMerkleRoot= getMerkleRoot();
		this.timeStamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		this.preBlockHash= hashPreviousBlock;
	
		
	}
	
	@Override
	public String toString() {
		return "BlockHeader [version=" + version + ", hashPreviousBlock=" + preBlockHash + ", hashMerkleRoot="
				+ hashMerkleRoot + ", publicKey=" + publicKey + ", number=" + index + ", timeStamp=" + timeStamp
				+ "]";
	}

	
	public String getVersion(){
		return version;
	}
	
	public void setVersion(String version){
		this.version=version;
	}
	
	public  String gethashPreviousBlock(){
		return preBlockHash;
	}
	
	public void setHashPreviousBlock(String hashPreviousBlock){
		this.preBlockHash=hashPreviousBlock;
	}
	
	public String getMerkleRoot(){
		
		return hashMerkleRoot;  
	 	//return new MerkleTree(contents).getRoot().getHash();
		
	}
	
	public void setHashMerkleRoot(String hashMerkleRoot){
		this.hashMerkleRoot=hashMerkleRoot;
	}

	public String getPublicKey(){
		return publicKey;
	}

	public void setPublicKey(String publicKey){
		this.publicKey=publicKey;
	}

	



	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getTimeStamp(){
		return new Date().getTime();
	}

	public void setTimestamp(String timeStamp){
		this.timeStamp=timeStamp;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
	
	
}