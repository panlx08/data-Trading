package hfut.hu.BlockValueShare.pbft.queue;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.pbft.msg.VoteMsg;
import hfut.hu.BlockValueShare.pbft.queue.AbstractVoteMsgQueue;
import hfut.hu.BlockValueShare.pbft.queue.PreMsgQueue;

/**
 * Confirm阶段的消息队列
 * 每个节点收到超过2f+1个不同节点（包括自己）的commit消息后，就认为该区块已经达成一致，进入committed状态，并将其持久化到区块链数据库中
 *
 * @author wuweifeng wrote on 2018/4/25.
 */
@Component
public class CommitMsgQueue extends AbstractVoteMsgQueue {
    @Resource
    private PreMsgQueue preMsgQueue;
    private Logger logger = LoggerFactory.getLogger(getClass());
    
//    public CommitMsgQueue() {
//  		this.peerNetwork = peerNetwork;
//  	}
	@Override
    protected void deal(VoteMsg voteMsg, List<VoteMsg> voteMsgs) {
        String hash = voteMsg.getHash();

        //通过校验agree数量，来决定是否在本地生成Block
        long count = voteMsgs.stream().filter(VoteMsg::isAgree).count();
        logger.info("已经commit为true的数量为:"+ count);
        if (count >= pbftAgreeSize()) {
            Block block = preMsgQueue.findByHash(hash);
            if (block == null) {
                return;
            }
            //本地落地
            voteStateConcurrentHashMap.put(hash, true);
            setNeed(true);
            clearOldBlockHash(block.getBlockHeader().getIndex());
            //2019/5/19 增加将该区块添加到本地存储区块链
            //add(block) 将该block添加到本地BlockChain中
            //ApplicationContextProvider.publishEvent(new AddBlockEvent(block));
        }
    }

    /**
     * 新区块生成后，clear掉map中number比区块小的所有数据
     */
    public void blockGenerated( Block block ) {
        clearOldBlockHash(block.getBlockHeader().getIndex());
    }

}
