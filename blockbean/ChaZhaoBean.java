package hfut.hu.BlockValueShare.blockbean;

public class ChaZhaoBean {

	//类型
	private String type;
	//数量
	private int num;
	//公钥
	private String publicKey;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	@Override
	public String toString() {
		return "PingJiaBean [score=" + type + ", evaluate=" + publicKey + ", find=" + num + "]";
	}
	public static void main(String[] args) throws Exception {
		ChaZhaoBean bean = new ChaZhaoBean();
		bean.setNum(1);
		bean.setPublicKey("1saddddddddddddddddddd");
		bean.setType("usual");
		System.out.print(bean.toString());
	}
}
