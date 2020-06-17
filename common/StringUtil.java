package hfut.hu.BlockValueShare.common;

import com.google.gson.GsonBuilder;

import hfut.hu.BlockValueShare.blockbean.Transaction;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class StringUtil {

    public static String applySha256(String input) {
        try {  
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();   
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);  
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);   
        }
    }

    /**
     * 	生成签名
     * @param privateKey : 付款人的私钥
     * @param input : 需要加密的数据信息
     * @return 返回字节数据
     * Applies ECDSA Signature and returns the result ( as bytes ).
     */
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    /**
     * 
     * 	验证签名
     *	@param publicKey ：公钥
     *	@param data ：加密的数据
     *	@param signature ：签名
     * Verifies a String signature
     */
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
  //Tacks in array of transactions and returns a merkle root.
    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
    		int count = transactions.size();
    		ArrayList<String> previousTreeLayer = new ArrayList<String>();
    		for(Transaction transaction : transactions) {
    			previousTreeLayer.add(transaction.getTransactionId());
    		}
    		ArrayList<String> treeLayer = previousTreeLayer;
    		while(count > 1) {
    			treeLayer = new ArrayList<String>();
    			for(int i=1; i < previousTreeLayer.size(); i++) {
    				treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
    			}
    			count = treeLayer.size();
    			previousTreeLayer = treeLayer;
    		}
    		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    		return merkleRoot;
    	}
    /*
     * 交易链
     */
    public static String getMerkleRoot2(ArrayList<Transaction> transactions) {
        int count = transactions.size();

        List<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.transactionId);
        }
        List<String> treeLayer = previousTreeLayer;

        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i+=2) {
                treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }
    //将对象转为字符串
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
    //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
   
    //转为字符串
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

   

    
}
