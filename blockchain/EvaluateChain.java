package hfut.hu.BlockValueShare.blockchain;

import java.util.ArrayList;
import java.util.List;


import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.blockbean.EvaluateBlock;
import hfut.hu.BlockValueShare.common.StringUtil;

public class EvaluateChain {
	 // 存储评价测链
    private List<EvaluateBlock> evaluateChain;
    private int average;
    public EvaluateChain() {
        // 初始化区块链
    	evaluateChain = new ArrayList<EvaluateBlock>();
    }
    /**返回评价内容
     * 
     */
    public static String GetPingjia(List<EvaluateBlock> evaluateChain) {
    	String string = null;
    	int index = 0;
    	while (index < evaluateChain.size()) {
    		if (!evaluateChain.get(index).getEvaluate().isEmpty()) {
				string = evaluateChain.get(index).getEvaluate()+"   ";
			}
    		
    		index++;
    	}
    	return string;
    }
    
    /**
	 **计算区块的分数
	 * 
	 * @param score
	 */
    public static int GetScore(List<EvaluateBlock> evaluateChain) {
		int score = 10;
		int index = 0;
		int sum = 0;
		while (index < evaluateChain.size()) {
			if (index == 1) {
				sum = sum + evaluateChain.get(index).getScore();
			}else {
				sum = sum + evaluateChain.get(index).getScore();
			}
			index++;
		}
		score = sum/(index+1);				
    	return score;
	}
    /**
	 * 计算区块的hash值
	 * 
	 * @param block
	 *            区块
	 * @return
	 */
	public static String calculateHash(EvaluateBlock block) {
		String getBlockHash = StringUtil.applySha256(block.getEvaluate());
		return getBlockHash;
	}

    /**
	 * 区块的生成
	 * 
	 * @param block
	 * @param vac
	 * @return
     * @throws Exception 
	 */
	public static EvaluateBlock generateBlock(Block block, List<String> vac) throws Exception {
		EvaluateBlock oldBlock = block.lastEva();
		EvaluateBlock newBlock = new EvaluateBlock();
		//序号
		newBlock.setIndex(oldBlock.getIndex()+1);
		String quality = vac.get(0);
		vac.remove(0);
		//存入评价
		newBlock.setEvaluate(quality);
		int score = Integer.parseInt(vac.get(0));
		vac.remove(0);
		//存入分数
		newBlock.setScore(score);
		//区块信息
		newBlock.setPreHash(oldBlock.getBlockHash());
		newBlock.setBlockHash(calculateHash(newBlock));
		return newBlock;
		
	}  
	public static EvaluateBlock generateBlock(Block block) throws Exception {
		EvaluateBlock oldBlock = block.lastEva();
		EvaluateBlock newBlock = new EvaluateBlock();
		//序号
		newBlock.setIndex(oldBlock.getIndex()+1);
		String quality = "good";
		//存入评价
		newBlock.setEvaluate(quality);
		int score = 10;
		//存入分数
		newBlock.setScore(score);
		//区块信息
		newBlock.setPreHash(oldBlock.getBlockHash());
		newBlock.setBlockHash(calculateHash(newBlock));
		return newBlock;
		
	}  
	public static EvaluateBlock generateBlock(EvaluateBlock oldBlock) throws Exception {
		EvaluateBlock newBlock = new EvaluateBlock();
		//序号
		newBlock.setIndex(oldBlock.getIndex()+1);
		String quality = "10";
		newBlock.setScore(10);
		//存入评价
		newBlock.setEvaluate(quality);
		//区块信息
		newBlock.setPreHash(oldBlock.getBlockHash());
		newBlock.setBlockHash(calculateHash(newBlock));
		return newBlock;
		
	}  
	
	
	/**
	 * 校验区块的合法性（有效性）
	 * 
	 * @param newBlock
	 * @param oldBlock
	 * @return
	 */
	public static boolean isBlockValid(EvaluateBlock newBlock, EvaluateBlock oldBlock) {
		if (oldBlock.getIndex() + 1 != newBlock.getIndex()) {
			return false;
		}
		if (!oldBlock.getBlockHash().equals(newBlock.getPreHash())) {
			return false;
		}
		return true;
	}

	
    /**
     * @return 得到区块链中的最后一个区块
     */
    public EvaluateBlock lastBlock() {
        return getEvaluateChain().get(getEvaluateChain().size() - 1);
    }

	public List<EvaluateBlock> getEvaluateChain() {
		return evaluateChain;
	}
	public void setEvaluateChain(List<EvaluateBlock> evaluateChain) {
		this.evaluateChain = evaluateChain;
	}
	public int getAverage() {
		return average;
	}
	public void setAverage(int average) {
		this.average = average;
	}

}
