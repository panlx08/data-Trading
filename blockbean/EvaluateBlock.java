package hfut.hu.BlockValueShare.blockbean;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 ** 评价侧链
 */
public class EvaluateBlock {
	//区块hash
	private String blockHash;
	//前区块hash
	private String preHash;
	//对应描述
	private String whatUsed;
	//分数
	private int score;
	//评价
	private String evaluate;
	//评价2
	private String evaluate2;
	//序号
	private int index;
	///时间戳
	private String timeStamp;
	
	public EvaluateBlock() {
		this.index= 0;
		this.score= 10;
		this.evaluate = "效果不错";
		this.evaluate2 = "效果不错";
		this.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	public EvaluateBlock(String evaluate) {
		this.index= 0;
		this.score = 10;
		this.evaluate = evaluate;
		this.evaluate2 = "效果不错";
		this.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	public EvaluateBlock(String evaluate1,String evaluate2,int i) {
		this.index= 0;
		this.evaluate = evaluate1;
		this.evaluate2 = evaluate2;
		this.score = i;
		this.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	public EvaluateBlock(int i) {
		this.index= 0;
		this.score = i;
		this.evaluate = "效果不错";
		this.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPreHash() {
		return preHash;
	}
	public void setPreHash(String preHash) {
		this.preHash = preHash;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getWhatUsed() {
		return whatUsed;
	}
	public void setWhatUsed(String whatUsed) {
		this.whatUsed = whatUsed;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public String toString() {
		return "EvaluateBlock [whatUsed=" + whatUsed + ", score=" + score + ", evaluate=" + evaluate + ", index="
				+ index + ", timeStamp=" + timeStamp + "]";
	}
	public String getEvaluate2() {
		return evaluate2;
	}
	public void setEvaluate2(String evaluate2) {
		this.evaluate2 = evaluate2;
	}
	
}
