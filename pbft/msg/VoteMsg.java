package hfut.hu.BlockValueShare.pbft.msg;

public class VoteMsg {
	//TODO 消息需要编号
	 /**
     * 当前投票状态（Prepare，commit）
     */
    private byte voteType;
    /**
     * 2019/5/20 给消息编号
     */
    private String order;
    /**
     * 区块的hash
     */
    private String hash;
    /**
     * 区块的number或者index
     */
    private int number;
    /**
     * 是哪个节点传来的
     */
    private String appId;
    /**
     * 是否同意
     */
    private boolean agree;

    @Override
    public String toString() {
        return "VoteMsg{" +
                "voteType=" + voteType +
                ", order='" + order + '\'' +
                ", hash='" + hash + '\'' +
                ", number=" + number +
                ", appId='" + appId + '\'' +
                ", agree=" + agree +
                '}';
    }

    public byte getVoteType() {
        return voteType;
    }

    public void setVoteType(byte voteType) {
        this.voteType = voteType;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
