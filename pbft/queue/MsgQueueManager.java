package hfut.hu.BlockValueShare.pbft.queue;

import org.springframework.stereotype.Component;

import hfut.hu.BlockValueShare.pbft.VoteType;
import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;
import hfut.hu.BlockValueShare.pbft.queue.BaseMsgQueue;
import hfut.hu.BlockValueShare.pbft.queue.CommitMsgQueue;
import hfut.hu.BlockValueShare.pbft.queue.PreMsgQueue;
import hfut.hu.BlockValueShare.pbft.queue.PrepareMsgQueue;

/**
 * 2019/5/17
 */
@Component
public class MsgQueueManager {
	public boolean need;
	
//	public MsgQueueManager(PeerNetwork peerNetwork) {
//		this.peerNetwork = peerNetwork;
//	}
    public void pushMsg(VotePreMsg voteMsg) throws Exception {
    	BaseMsgQueue baseMsgQueue = null;
        switch (voteMsg.getVoteType()) {
            case VoteType
                    .PREPREPARE:
                baseMsgQueue = new PreMsgQueue();
                break;
            case VoteType.PREPARE:
                baseMsgQueue = new PrepareMsgQueue();
                break;
            case VoteType.COMMIT:
                baseMsgQueue = new CommitMsgQueue();
                break;
            default:
                break;
        }
        if(baseMsgQueue != null) {
//        	System.out.println("zhe"+voteMsg.getHash());
        	baseMsgQueue.push(voteMsg);
        	switch (voteMsg.getVoteType()) {
        		case VoteType.COMMIT:
        			baseMsgQueue = new CommitMsgQueue();
        			setNeed(baseMsgQueue.need);
        	}
        	
        }
    }
	public boolean isNeed() {
		return need;
	}
	public void setNeed(boolean need) {
		this.need = need;
	}
}

