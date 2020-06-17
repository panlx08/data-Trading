package hfut.hu.BlockValueShare.dh;


import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
  
import javax.crypto.Cipher;

import org.apache.commons.lang3.ArrayUtils;
  
/** 
 * RSA加密与解密,采用Base64编码
 * 
 * @author XerCis
 */  
public class RSAProvider {  
  
    public static final String KEY_ALGORITHM = "RSA";//指定算法为RSA
    public static int KEYSIZE = 512;//加密Key的长度
    public static int decodeLen = KEYSIZE / 8;//解密时必须按照此分组解密
    public static int encodeLen = 100;//加密时小于117即可 
    private static final String PUBLIC_KEY = "publicKey";//公钥
    private static final String PRIVATE_KEY = "privateKey";//私钥 
    private static final String MODULES = "RSAModules";//MODULES
  
    /** 
     *  
     * 生成KeyPair 
     * @return Map<String, Object>
     * @throws Exception 
     * @author XerCis
     */  
    public static Map<String, Object> generateKeyPair() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEYSIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
  
        
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();//公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();//私钥 
        BigInteger modules = privateKey.getModulus();//参数n
  
        Map<String, Object> keys = new HashMap<String, Object>(3);
        keys.put(PUBLIC_KEY, publicKey);
        keys.put(PRIVATE_KEY, privateKey);
        keys.put(MODULES, modules);
        return keys;
    }
  
    /** 
     *  
     * 加密public key 
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    public static byte[] encryptPublicKey(String data, String key) throws Exception {  
        byte[] bytes = data.getBytes("UTF-8");
        byte[] encode = new byte[] {};
        for (int i = 0;i < bytes.length;i += encodeLen) {  
            byte[] subarray = ArrayUtils.subarray(bytes, i, i + encodeLen);
            byte[] doFinal = encryptByPublicKey(subarray, key);
            encode = ArrayUtils.addAll(encode, doFinal);
        }  
        return encode;
    }  
  
    /** 
     * 解密 private key 
     * 方法说明。 
     * @param encode 
     * @param key 
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    public static String decryptPrivateKey(byte[] encode, String key) throws Exception {  
        byte [] buffers = new byte[]{};
        for (int i = 0;i < encode.length;i += decodeLen) {  
            byte[] subarray = ArrayUtils.subarray(encode, i, i + decodeLen);
            byte[] doFinal = decryptByPrivateKey(subarray, key);
            buffers = ArrayUtils.addAll(buffers, doFinal);
        }  
        return new String(buffers, "UTF-8");
    }  
  
    /** 
     * 用私钥解密  
     * @param data 
     * @param keyBytes 
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    private static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {  
        // 对密钥解密  取得私钥    
        byte[] keyBytes = StreamUtil.base64ToByte(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
  
        // 对数据解密     
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
  
        return cipher.doFinal(data);
    }  
  
    /** 
     *  
     * 用公钥加密  
     * @param data 
     * @param keyBytes 
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    private static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {  
        // 对公钥解密     
        // 取得公钥    
        byte[] keyBytes = StreamUtil.base64ToByte(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
  
        // 对数据加密     
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
  
        return cipher.doFinal(data);
    }
  
    /** 
     * 取得加密参数n
     *  
     * @return 
     * @author XerCis 
     */  
    public static byte[] getModulesBytes(Map<String, Object> keys) {  
        BigInteger big = (BigInteger) keys.get(MODULES);
        return big.toByteArray();
    }  
  
    /** 
     *  
     * 取得私钥  
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    public static String getPrivateKeyBytes(Map<String, Object> keys) throws Exception {  
        Key key = (Key) keys.get(PRIVATE_KEY);
        return StreamUtil.byteToBase64(key.getEncoded());
    }  
  
    /** 
     * 取得公钥  
     *  
     * @return 
     * @throws Exception 
     * @author XerCis 
     */  
    public static String getPublicKeyBytes(Map<String, Object> keys) throws Exception {  
        Key key = (Key) keys.get(PUBLIC_KEY);
        return StreamUtil.byteToBase64(key.getEncoded());
    }
    
}