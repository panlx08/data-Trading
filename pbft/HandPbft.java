package hfut.hu.BlockValueShare.pbft;

//import hfut.hu.BlockValueShare.pbft.msg.VoteMsg;
import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;
import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.pbft.queue.MsgQueueManager;
import hfut.hu.BlockValueShare.pbft.queue.PrepareMsgQueue;
import hfut.hu.BlockValueShare.common.CommonUtil;

public class HandPbft {

	private MsgQueueManager msgmanager = new MsgQueueManager();
	
//	public HandPbft(PeerNetwork peerNetwork) {
//		this.peerNetwork = peerNetwork;
//	}
	//开始进行pbf投票
	public static VotePreMsg topbft(Block block) throws Exception {
		VotePreMsg votepremsg =new VotePreMsg();
		String order = CommonUtil.generateUuid();
		votepremsg.setOrder(order);
		votepremsg.setBlock(block);
		votepremsg.setHash(votepremsg.getBlock().blockHash);
		votepremsg.setAppId(PrepareMsgQueue.IP());
		votepremsg.setVoteType(VoteType.PREPREPARE);
		votepremsg.setNumber(votepremsg.getBlock().getBlockHeader().getIndex());
		return votepremsg;
	}
	//
	public boolean pbfthandle(VotePreMsg votepremsg) throws Exception{
		msgmanager.pushMsg(votepremsg);
		return msgmanager.need;
	}
}
