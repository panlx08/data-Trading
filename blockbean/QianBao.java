package hfut.hu.BlockValueShare.blockbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QianBao {
	public String publicKey;
	public String privateKey;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	public QianBao(String publicKey,	String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	public float getBalance(HashMap<String,TransactionOutput> LUTXOs) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: LUTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.value ;
            }
        }
        return total;
    }
	 public Transaction sendFunds(HashMap<String,TransactionOutput> LUTXOs,String _recipient,float value ) {
	        if(getBalance(LUTXOs) < value) {
	            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
	            return null;
	        }
	        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	        
	        float total = 0;
	        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
	            TransactionOutput UTXO = item.getValue();
//	            System.out.println("walleta de "+UTXOs);
	            total += UTXO.value;
	            inputs.add(new TransactionInput(UTXO.id));
//	            System.out.println(inputs.get(0).transactionOutputId);
	            if(total > value) {
	            	break;
	            }
	        }
//	        System.out.println(inputs.get(0).UTXO);
	        
	        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
	        try {
				newTransaction.generateSignature(privateKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//	        System.out.println("9");

	        for(TransactionInput input: inputs){
//	        	System.out.println("9"+UTXOs.get(input.transactionOutputId));
	            UTXOs.remove(input.transactionOutputId);
//	            System.out.println("9"+UTXOs.get(input.transactionOutputId));
	        }
//	        System.out.println("haaaaishi这里"+inputs.get(0).transactionOutputId);
//	        System.out.println("hi这里"+LUTXOs.get(inputs.get(0).transactionOutputId));
	        return newTransaction;
	    }

}
