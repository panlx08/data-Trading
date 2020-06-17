package hfut.hu.BlockValueShare.blockbean;

public class PingJiaBean {
	//分数
	private int score;
	//评价
	private String evaluate;
	//评价2
	private String evaluate2;
	//哪一项
	private int find;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	public int getFind() {
		return find;
	}
	public void setFind(int find) {
		this.find = find;
	}
	
	@Override
	public String toString() {
		return "PingJiaBean [score=" + score + ", evaluate=" + evaluate + ", evaluate2=" + evaluate2 + ", find=" + find
				+ "]";
	}
	public static void main(String[] args) throws Exception {
		PingJiaBean bean = new PingJiaBean();
		bean.setEvaluate("还不错");
		bean.setFind(2);
		bean.setScore(80);
		System.out.print(bean.toString());
	}
	public String getEvaluate2() {
		return evaluate2;
	}
	public void setEvaluate2(String evaluate2) {
		this.evaluate2 = evaluate2;
	}
}
