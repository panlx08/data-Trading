package hfut.hu.BlockValueShare.pbft.queue;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import hfut.hu.BlockValueShare.blockbean.Block;
import hfut.hu.BlockValueShare.common.timer.TimerManager;
import hfut.hu.BlockValueShare.pbft.VoteType;

import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;
import hfut.hu.BlockValueShare.pbft.queue.BaseMsgQueue;
import hfut.hu.BlockValueShare.pbft.queue.PrepareMsgQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * preprepare消息的存储，但凡收到请求生成Block的信息，都在这里处理
 *
 * @author wuweifeng wrote on 2018/4/23.
 */
@Component
public class PreMsgQueue extends BaseMsgQueue {
    private PrepareMsgQueue prepareMsgQueue;
    
    final Gson gson = new GsonBuilder().create();
    private ConcurrentHashMap<String, VotePreMsg> blockConcurrentHashMap = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public PreMsgQueue() { 	
  		prepareMsgQueue = new PrepareMsgQueue();
  	}
    @Override
    protected void push(VotePreMsg voteMsg) throws Exception {
        //该队列   	
    	int num = voteMsg.getNumber();
        String hash = voteMsg.getHash();
        
        //避免收到重复消息
        if (blockConcurrentHashMap.get(hash) != null) {
            return;
        }
        //但凡是能进到该push方法的，都是通过基本校验的，但在并发情况下可能会相同number的block都进到投票队列中
        //需要对新进来的Vote信息的number进行校验，如果在比prepre阶段靠后的阶段中，已经出现了认证OK的同number的vote，则拒绝进入该队列
        if (prepareMsgQueue.otherConfirm(hash, num)) {
            logger.info("拒绝进入Prepare阶段，hash为" + hash);
            return;
        }
        //存入Pre集合中
        blockConcurrentHashMap.put(hash, voteMsg);

        //加入Prepare行列，推送给所有人
        VotePreMsg prepareMsg = voteMsg;
        
        prepareMsg.setVoteType(VoteType.PREPARE);
        prepareMsg.setAppId(PrepareMsgQueue.IP());
        
        //将prepre消息广
        System.out.println();
//        peerNetwork.broadcast("PBFT "+gson.toJson(prepareMsg));
        blockConcurrentHashMap.remove(hash);
    }

    /**
     * 根据hash，得到内存中的Block信息
     *
     * @param hash
     *         hash
     * @return Block
     */
    public Block findByHash(String hash) {
        VotePreMsg votePreMsg = blockConcurrentHashMap.get(hash);
        if (votePreMsg != null) {
            return votePreMsg.getBlock();
        }
        return null;
    }

    /**
     * 	新区块生成后，clear掉map中number比区块小的所有数据
     */
    public void blockGenerated(Block block) {
        int number = block.getBlockHeader().getIndex();
        TimerManager.schedule(() -> {
            for (String key : blockConcurrentHashMap.keySet()) {
                if (blockConcurrentHashMap.get(key).getNumber() <= number) {
                    blockConcurrentHashMap.remove(key);
                }
            }
            return null;
        }, 2000);
    }
}

