package hfut.hu.BlockValueShare.pbft.queue;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import hfut.hu.BlockValueShare.pbft.msg.VotePreMsg;

/**
 * 各节点互传的投票消息存储队列基类
 *2019/5/17
 */
@Component
public abstract class BaseMsgQueue {
    public boolean need;
	public void setNeed(boolean need) {
		this.need = need;
	}

	@Resource
   //2019/5/17 private ClientStarter clientStarter;

    public int pbftSize() {
    	return 1;
        //2019/5/17 return clientStarter.pbftSize();
    }

    public int pbftAgreeSize() {
    	return 2;
        //2019/5/17 return clientStarter.pbftAgreeCount();
    }
    /**
     * 来了新消息
     *
     * @param voteMsg
     *         voteMsg
     * @throws Exception 
     */
    protected abstract void push(VotePreMsg voteMsg) throws Exception;
}
