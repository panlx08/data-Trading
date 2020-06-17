package hfut.hu.BlockValueShare.pbft.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import hfut.hu.BlockValueShare.common.timer.TimerManager;
import hfut.hu.BlockValueShare.pbft.msg.VoteMsg;
import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;
import hfut.hu.BlockValueShare.pbft.queue.BaseMsgQueue;

import cn.hutool.core.collection.CollectionUtil;

public abstract class AbstractVoteMsgQueue extends BaseMsgQueue {
    /**
     * 存储所有的hash的投票集合
     */
    protected ConcurrentHashMap<String, List<VoteMsg>> voteMsgConcurrentHashMap = new ConcurrentHashMap<String, List<VoteMsg>>();
    /**
     * 存储本节点已确认状态的hash的集合，即本节点已对外广播过允许commit或拒绝commit的消息了
     */
    protected ConcurrentHashMap<String, Boolean> voteStateConcurrentHashMap = new ConcurrentHashMap<String, Boolean>();

    abstract void deal(VoteMsg voteMsg, List<VoteMsg> voteMsgs) throws Exception;

    @Override
    protected void push(VotePreMsg voteMsg) throws Exception {
        String hash = voteMsg.getHash();
        List<VoteMsg> voteMsgs = voteMsgConcurrentHashMap.get(hash);
        if (CollectionUtil.isEmpty(voteMsgs)) {
            voteMsgs = new ArrayList<VoteMsg>();
            voteMsgConcurrentHashMap.put(hash, voteMsgs);
        } else {
            //如果不空的情况下，判断本地集合是否已经存在完全相同的voteMsg了
            for (VoteMsg temp : voteMsgs) {
                if (temp.getAppId().equals(voteMsg.getAppId())) {
                    return;
                }
            }
        }

        //添加进去
        voteMsgs.add(voteMsg);
        //如果已经对该hash投过票了，就不再继续
        if (voteStateConcurrentHashMap.get(hash) != null) {
            return;
        }

        deal(voteMsg, voteMsgs);
    }

    /**
     * 该方法用来确认待push阶段的下一阶段是否已存在已达成共识的Block <p>
     * 譬如收到了区块5的Prepare的投票信息，那么我是否接受该投票，需要先校验Commit阶段是否已经存在number>=5的投票成功信息
     *
     * @param hash
     *         hash
     * @return 是否超过
     */
    public boolean hasOtherConfirm(String hash, int number) {
        if (voteMsgConcurrentHashMap.size()>0) {
			//遍历该阶段的所有投票信息
			for (String key : voteMsgConcurrentHashMap.keySet()) {
				//如果下一阶段存在同一个hash的投票，则不理会
				if (hash.equals(key)) {
					continue;
				}
				//如果下一阶段的number比当前投票的小，则不理会
				if (voteMsgConcurrentHashMap.get(key).get(0).getNumber() < number) {
					continue;
				}
				//只有别的>=number的Block已经达成共识了，则返回true，那么将会拒绝该hash进入下一阶段
				if (voteStateConcurrentHashMap.get(key) != null && voteStateConcurrentHashMap.get(key)) {
					return true;
				}
			} 
		}
		return false;
    }

    /**
     *	 清理旧的block的hash
     * 
     */
    protected void clearOldBlockHash(int number) {
    	TimerManager.schedule(() -> {
            for (String key : voteMsgConcurrentHashMap.keySet()) {
                if (voteMsgConcurrentHashMap.get(key).get(0).getNumber() <= number) {
                    voteMsgConcurrentHashMap.remove(key);
                    voteStateConcurrentHashMap.remove(key);
                }
            }
            return null;
        },2000);
    }
}

