package hfut.hu.BlockValueShare.blockbean;

import java.io.Serializable;

import hfut.hu.BlockValueShare.common.StringUtil;



public class TransactionOutput implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String reciepient; //也被称为这些硬币的新主人。
	public float value; //他们拥有的硬币数量
	public String parentTransactionId; //这个输出的id
	
	//Constructor
    public TransactionOutput(String reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(reciepient+Float.toString(value)+parentTransactionId);
    }
	
	@Override
	public String toString() {
		return "TransactionOutput [id=" + id + ", reciepient=" + reciepient + ", value=" + value
				+ ", parentTransactionId=" + parentTransactionId + "]";
	}

	//Check if coin belongs to you
	public boolean isMine(String publicKey) {
		return (publicKey.equals(reciepient));
	}
	
}