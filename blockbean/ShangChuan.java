package hfut.hu.BlockValueShare.blockbean;

import java.util.ArrayList;

public class ShangChuan {
	/* 
	 * 	新的JSON内容
	 */
	private ArrayList<String> dataSummary;
	/* 
	 * 	该数据的数量
	 */
	
	private ArrayList<String> storageProof;
	private ArrayList<String> dataUid;
	private ArrayList<String> dataSign;
	private ArrayList<String> dataHash;
	
	private int num;
	/* 
	 * 	该数据的价格
	 */
	private int cost;
	/* 
	 * 	该数据的类型
	 */
	private String type;
	/* 
	 * 	该数据的公钥地址
	 */
	
//	private int sp;
	/* 
	 * 	该数据的存储证明
	 */
	
	
	private String pubKey;
	/* 
	 * 	该数据的ip
	 */
	private String ip;
	public String getPubKey() {
		return pubKey;
	}
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public ArrayList<String> getDataSummary() {
		return dataSummary;
	}
	public void setDataSummary(ArrayList<String> dataSummary) {
		this.dataSummary = dataSummary;
	}
	public ArrayList<String> getStorageProof() {
		return storageProof;
	}
	public void setStorageProof(ArrayList<String> storageProof) {
		this.storageProof = storageProof;
	}
	
	public ArrayList<String> getDataUid() {
		return dataUid;
	}
	public void setDataUid(ArrayList<String> dataUid) {
		this.dataUid = dataUid;
	}
	public ArrayList<String> getDataSign() {
		return dataSign;
	}
	public void setDataSign(ArrayList<String> dataSign) {
		this.dataSign = dataSign;
	}
	
	public ArrayList<String> getDataHash() {
		return dataHash;
	}
	public void setDataHash(ArrayList<String> dataHash) {
		this.dataHash = dataHash;
	}
	
	/*
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	*/
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ShangChuan [dataSummary=" + dataSummary + ", storageProof=" + storageProof + ",dataUid=" + dataUid + ",dataSign=" + dataSign + ",dataHash=" + dataHash + ", num=" + num + ", cost=" + cost + ", type=" + type
				+ ", pubKey=" + pubKey + "]";
	}
	
}
