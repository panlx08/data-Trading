package hfut.hu.BlockValueShare.blockbean;

public class FenShu {
	//序号
	private int index;
	//数量
	private int num;
	//ip
	private String ip;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public FenShu() {
		this.index= 0;
		this.num = 0;
	}
	public FenShu(int index,int num,String ip) {
		this.index= index;
		this.num = num;
		this.ip = ip;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	/**
	 * @author 
	 * @param v 做标准化的样本数据
	 * @param Min 样本数据最小值
	 * @param Max 样本数据最大值
	 * @param newMin 新的映射区间最小值
	 * @param newMax 新的映射区间最大值
	 * @return
	 */
//	public double Normalization(double v, double Min, double Max, double newMin, double newMax) {
//		return (v-Min)/(Max-Min)*(newMax-newMin)+newMin;
//	}
	public static double Normalization(double v, double Min, double Max) {
		return (v-Min)/(Max-Min);
	}
}
