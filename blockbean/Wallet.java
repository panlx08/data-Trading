package hfut.hu.BlockValueShare.blockbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hfut.hu.BlockValueShare.common.ECDSAAlgorithm;

/*
 * 	椭圆曲线
 * 	创建公钥私钥
 */
public class Wallet {
    public String privateKey;
    public String publicKey;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.

    public Wallet(){
        generateKeyPair();  
        
    }
    public HashMap<String, TransactionOutput> getUTXOs() {
		return UTXOs;
	}
	public void setUTXOs(HashMap<String, TransactionOutput> uTXOs) {
		UTXOs = uTXOs;
	}
	public void generateKeyPair() {
        try {
        	 privateKey = ECDSAAlgorithm.generatePrivateKey();                  //产生身份私钥
        	 publicKey = ECDSAAlgorithm.generatePublicKey(privateKey, true);    //产生身份公钥
        	 
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public float getBalance(HashMap<String,TransactionOutput> LUTXOs) {         //查询用户余额
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: LUTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) {                                       //输入为自身公钥地址 )
                UTXOs.put(UTXO.id,UTXO);                                       //作为未花费的交易列表
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
//            System.out.println("walleta de "+UTXOs);
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
//            System.out.println(inputs.get(0).transactionOutputId);
            if(total > value) {
            	break;
            }
        }
//        System.out.println(inputs.get(0).UTXO);
        
        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        try {
			newTransaction.generateSignature(privateKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        System.out.println("9");

        for(TransactionInput input: inputs){
//        	System.out.println("9"+UTXOs.get(input.transactionOutputId));
            UTXOs.remove(input.transactionOutputId);
//            System.out.println("9"+UTXOs.get(input.transactionOutputId));
        }
//        System.out.println("haaaaishi这里"+inputs.get(0).transactionOutputId);
//        System.out.println("hi这里"+LUTXOs.get(inputs.get(0).transactionOutputId));
        return newTransaction;
    }


    
}