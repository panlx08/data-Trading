package hfut.hu.BlockValueShare.blockbean;

public class TransactionInput {
	public String transactionOutputId; //参考TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //包含未使用的transaction output
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
}