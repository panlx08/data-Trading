package hfut.hu.BlockValueShare.blockbean;

import java.util.ArrayList;
import java.util.List;
/* 
 * @区块body内的一条内容实体
 **存储数据摘要
 * @author
 */
public class ContentInfo{
	/* 
	 * 	新的JSON内容
	 */
	private ArrayList<String> dataSummary;
	/* 
	 * 	该数据的数量
	 */
	private int num;
	/* 
	 * 	该数据的价格
	 */
	private int cost;
	
	private ArrayList<String> storageProof;
	/* 
	 * 	该数据的存储证明
	 */
	private ArrayList<String> dataUid;
	private ArrayList<String> dataSign;
	private ArrayList<String> dataHash;
	
	public ContentInfo(ShangChuan shangChuan) {
		this.dataSummary = shangChuan.getDataSummary();
		this.num = shangChuan.getNum();
		this.cost = shangChuan.getCost();
//		this.sp = shangChuan.getSp();
		this.storageProof = shangChuan.getStorageProof();
		this.dataUid = shangChuan.getDataUid();
		this.dataSign = shangChuan.getDataSign();
		this.dataHash = shangChuan.getDataHash();
	}
	
	public ContentInfo(List<String> contents, String string) {
	}

	@Override
	public String toString() {
		return "ContentInfo [dataSummary=" + dataSummary + ", storageProof=" + storageProof + ",dataUid=" + dataUid + ",dataSign=" + dataSign + ",dataHash=" + dataHash + ", num=" + num + ", cost=" + cost  + "]";
	}
	public ArrayList<String> getJsonContent() {
		return dataSummary;
	}

	public void setJsonContent(List<String> jsonContent) {
		this.dataSummary = (ArrayList<String>) jsonContent;
	}
	public void addJsonContent(String content) {
		dataSummary.add(content);
	}
	
	public ArrayList<String> getJsonContent1() {
		return storageProof;
	}

	public void setJsonContent1(List<String> jsonContent) {
		this.storageProof = (ArrayList<String>) jsonContent;
	}
	public void addJsonContent1(String content) {
		storageProof.add(content);
	}
	
	
	
	public ArrayList<String> getJsonContent2() {
		return storageProof;
	}

	public void setJsonContent2(List<String> jsonContent) {
		this.storageProof = (ArrayList<String>) jsonContent;
	}
	public void addJsonContent2(String content) {
		storageProof.add(content);
	}
	
	public ArrayList<String> getJsonContent3() {
		return storageProof;
	}

	public void setJsonContent3(List<String> jsonContent) {
		this.storageProof = (ArrayList<String>) jsonContent;
	}
	public void addJsonContent3(String content) {
		storageProof.add(content);
	}
	
	public ArrayList<String> getJsonContent4() {
		return storageProof;
	}

	public void setJsonContent4(List<String> jsonContent) {
		this.storageProof = (ArrayList<String>) jsonContent;
	}
	public void addJsonContent4(String content) {
		storageProof.add(content);
	}
	
	
	
	public int getNum() {
		return num;
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
	public ArrayList<String> getdDataSign() {
		return dataSign;
	}
	public void setDataSign(ArrayList<String> dataSign) {
		this.dataSign = dataSign;
	}
	
	public ArrayList<String> getdDataHash() {
		return dataHash;
	}
	public void setDataHash(ArrayList<String> dataHash) {
		this.dataHash = dataHash;
	}
	
	
	public ArrayList<String> getDataSummary() {
		return dataSummary;
	}
	public void setDataSummary(ArrayList<String> dataSummary) {
		this.dataSummary = dataSummary;
	}
	
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
/*
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
*/	
	
	public int getPrice() {
		return cost;
	}
	public void setPrice(int price) {
		this.cost = price;
	}




	
//	public static void main(String []args) {
//		ContentInfo contentInfo =new ContentInfo(null);
//		String a =contentInfo.toString();
//		System.out.print(a);
//	}
}

