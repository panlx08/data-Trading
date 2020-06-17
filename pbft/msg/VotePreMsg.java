package hfut.hu.BlockValueShare.pbft.msg;

import hfut.hu.BlockValueShare.pbft.msg.VoteMsg;

import hfut.hu.BlockValueShare.blockbean.Block;

/**
 * 该Block实例存在不同 2019/5/17
 */

public class VotePreMsg extends VoteMsg{
	private Block block;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
