package hfut.hu.BlockValueShare.blockchain;

import java.util.ArrayList;
import java.util.List;

import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.blockbean.ContentInfo;
import hfut.hu.BlockValueShare.blockbean.EvaluateBlock;
import hfut.hu.BlockValueShare.blockbean.ShangChuan;
import hfut.hu.BlockValueShare.common.StringUtil;
/**
 * 关于链处理的公共方法类
 * @author 
 *
 */
public class BlockChain {
	 // 存储区块链
    private List<Block> chain;
//    // 该实例变量用于当前的交易信息列表
//    private List<Map<String, Object>> currentTransactions;

    public BlockChain() {
        // 初始化区块链以及当前的交易信息列表
        chain = new ArrayList<Block>();
//        currentTransactions = new ArrayList<Map<String, Object>>();

    }
    /**
	 * 计算区块的hash值
	 * 
	 * @param block
	 *            区块
	 * @return
	 */
	public static String calculateHash(Block block) {
		String getBlockHash = StringUtil.applySha256(block.getBlockHeader().toString()+
				block.getBlockBody().toString());
		return getBlockHash;
	}
    /**
	 * 区块的生成
	 * 
	 * @param oldBlock
	 * @param vac
	 * @return
     * @throws Exception 
	 */
	public static Block generateBlock(Block oldBlock, ShangChuan vac) throws Exception {
		Block newBlock = new Block();
		String type;
		newBlock.getBlockHeader().setIndex(oldBlock.getBlockHeader().getIndex() + 1);
		
		ContentInfo contentInfo = new ContentInfo(vac);
		List<ContentInfo> dataSet= new ArrayList<ContentInfo>();
		dataSet.add(contentInfo);
		newBlock.getBlockBody().setDataSet(dataSet);

		type = vac.getType();
		newBlock.getBlockHeader().setType(type);
		newBlock.setIp(vac.getIp());
		newBlock.getBlockBody().setPubKey(vac.getPubKey());
		newBlock.getBlockHeader().setHashPreviousBlock(oldBlock.getBlockHash());
		newBlock.setBlockHash(calculateHash(newBlock));
		
		EvaluateBlock eGenesisBlock = new EvaluateBlock();	
		eGenesisBlock.setPreHash(newBlock.getBlockHash());
		eGenesisBlock.setEvaluate("10");
		eGenesisBlock.setBlockHash(EvaluateChain.calculateHash(eGenesisBlock));
		newBlock.addEva(eGenesisBlock);
		return newBlock;	
	}
	
	/**
	 * 校验区块的合法性（有效性）
	 * 
	 * @param newBlock
	 * @param oldBlock
	 * @return
	 */
	public static boolean isBlockValid(Block newBlock, Block oldBlock) {
		if (oldBlock.getBlockHeader().getIndex() + 1 != newBlock.getBlockHeader().getIndex()) {
			return false;
		}
		if (!oldBlock.getBlockHash().equals(newBlock.getBlockHeader().gethashPreviousBlock())) {
			return false;
		}
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
	
    /**
     * @return 得到区块链中的最后一个区块
     */
    public Block lastBlock() {
        return getChain().get(getChain().size() - 1);
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }


}
