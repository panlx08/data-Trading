package hfut.hu.BlockValueShare.pbft.queue;

import java.net.InetAddress;
import java.util.List;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.pbft.VoteType;
import hfut.hu.BlockValueShare.pbft.msg.VoteMsg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.hutool.core.bean.BeanUtil;

/**
 * Prepare阶段的消息队列
 *
 * 2019/5/17
 */
@Component
public class PrepareMsgQueue extends AbstractVoteMsgQueue {
    @Resource
    private CommitMsgQueue commitMsgQueue ;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    final Gson gson = new GsonBuilder().create();
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public PrepareMsgQueue() {
    	commitMsgQueue = new CommitMsgQueue();
	}
    /**
     * 收到节点（包括自己）针对某Block的Prepare消息
     *
     * @param voteMsg
     *         voteMsg
     * @throws Exception 
     */
    @Override
    protected void deal(VoteMsg voteMsg, List<VoteMsg> voteMsgs) throws Exception {
        String hash = voteMsg.getHash();
        VoteMsg commitMsg = new VoteMsg();
        BeanUtil.copyProperties(voteMsg, commitMsg);
        commitMsg.setVoteType(VoteType.COMMIT);
        commitMsg.setAppId(IP());
        //开始校验并决定是否进入commit阶段
        //校验该vote是否合法
        if (commitMsgQueue.hasOtherConfirm(hash, voteMsg.getNumber())) {
             agree(commitMsg, false);
        } else {
            //开始校验拜占庭数量，如果agree的超过2f + 1，就commit
            long agreeCount = voteMsgs.stream().filter(VoteMsg::isAgree).count();
            long unAgreeCount = voteMsgs.size() - agreeCount;

            //开始发出commit的同意or拒绝的消息
            if (agreeCount >= pbftAgreeSize()) {
                agree(commitMsg, true);
            } else if (unAgreeCount >= pbftSize() + 1) {
                agree(commitMsg, false);
            }
        }

    }
    public static String IP() throws Exception {
    	return InetAddress.getLocalHost().getHostAddress().toString();
    }
    private void agree(VoteMsg commitMsg, boolean flag) {
        logger.info("Prepare阶段完毕，是否进入commit的标志是：" + flag);
        //发出拒绝commit的消息
        commitMsg.setAgree(flag);
        voteStateConcurrentHashMap.put(commitMsg.getHash(), flag);
        //2019/5/20 广播给所有节点
//        peerNetwork.broadcast("PBFTEND "+gson.toJson(commitMsg));
        //eventPublisher.publishEvent(new MsgCommitEvent(commitMsg));
    }

    /**
     * 判断大家是否已对其他的Block达成共识，如果true，则拒绝即将进入队列的Block
     *
     * @param hash
     *         hash
     * @return 是否存在
     */
    public boolean otherConfirm(String hash, int number) {
    	System.out.println("bbbbbbbbbb");
        if (commitMsgQueue.hasOtherConfirm(hash, number)) {
            return true;
        }
        return hasOtherConfirm(hash, number);
    }

    /**
     * 	新区块生成后，clear掉map中number比区块小的所有数据
     *
     * @param 
     */
    public void blockGenerated(Block block) {
        clearOldBlockHash(block.getBlockHeader().getIndex());
    }

}

