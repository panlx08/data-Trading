package hfut.hu.BlockValueShare.blockbean;

import java.util.ArrayList;
import java.util.HashMap;

import hfut.hu.BlockValueShare.blockchain.TradeChain;
import hfut.hu.BlockValueShare.common.ECDSAAlgorithm;
import hfut.hu.BlockValueShare.common.StringUtil;


/**
 * 交易类
 *
 */
public class Transaction {

    public String transactionId; //交易hash
    public String sender; //发送方地址/public key.
    public String reciepient; //接收方/public key.
    public float value; //包含我们希望发送给收件人的金额。
//    public transient String signature; //防止别人把钱花在我们的钱包里
    public String signature;
    public Boolean sign = true;//有签名
    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    public Boolean getSign() {
		if (signature.length() != 0) {
			return true;
		}else {
			return false;
		}  	
	}

	public void setSign(Boolean sign) {
		this.sign = sign;
	}

	private static int sequence = 0; //生成了多少事务的粗略计数
	
	/**
	 * 
	 * @param from 
	 * @param to
	 * @param value
	 * @param inputs
	 */
    public Transaction(String from, String to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }
    public Transaction() {
        
    }
   
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", sender=" + sender + ", reciepient=" + reciepient
				+ ", value=" + value + ", sign=" + sign + ", inputs=" + inputs + ", outputs=" + outputs + "]";
	}

	public boolean processTransaction(HashMap<String,TransactionOutput> LUTXOs) throws Exception {		
        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
       
        //收集交易输入(确保未使用):
        for(TransactionInput i : inputs) { 
//        	System.out.println("这里"+LUTXOs.get(i.transactionOutputId));
            i.UTXO = LUTXOs.get(i.transactionOutputId);
        }

        //检查交易是否有效:
        if(getInputsValue() < TradeChain.minimumTransaction) {
            System.out.println("Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //生成事务输出:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender

        //将输出添加到未使用列表
        for(TransactionOutput o : outputs) {
        	LUTXOs.put(o.id , o);
        }

        //从UTXO列表中删除已使用的事务输入:
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            LUTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
//            System.out.println("here: ");
            if(i.UTXO == null) continue; //如果无法找到事务跳过它，则此行为可能不是最优的。
            total += i.UTXO.value;
        }
        return total;
    }

    public void generateSignature(String privateKey) throws Exception {
        String data = sender + reciepient + Float.toString(value);
        signature = ECDSAAlgorithm.sign(privateKey,data);
    }

    public boolean verifySignature() throws Exception {
        String data = sender + reciepient + Float.toString(value)	;
        return ECDSAAlgorithm.verify(data, signature, sender);
    }

    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                sender +   reciepient +
                Float.toString(value) + sequence
       	);
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciepient() {
		return reciepient;
	}

	public void setReciepient(String reciepient) {
		this.reciepient = reciepient;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ArrayList<TransactionInput> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public ArrayList<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(ArrayList<TransactionOutput> outputs) {
		this.outputs = outputs;
	}

	public static int getSequence() {
		return sequence;
	}

	public static void setSequence(int sequence) {
		Transaction.sequence = sequence;
	}
    
}


