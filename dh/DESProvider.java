package hfut.hu.BlockValueShare.dh;


import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
  
/** 
 * DES加密与解密,采用Base64编码
 * 
 */  
public class DESProvider {  
  
    private static final String KEY_ALGORITHM = "DESede";//指定算法为DESedge
    private static int KEYSIZE = 168;//加密Key的长度
  
    /** 
     *  
     * 生成对称密钥
     * @return Map<String, Object>
     * @throws Exception 
     * @author XerCis
     * @throws NoSuchAlgorithmException 
     */  
    public static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(KEYSIZE);//初始化密钥生成器
		SecretKey k = kg.generateKey();//生成密钥
		byte[] keyBytes = k.getEncoded();//获取二进制编码形式
		return StreamUtil.byteToBase64(keyBytes);
    }
    
    /** 
     * 加密数据
     * @param data 待加密数据 
     * @param key 密钥 
     * @return byte[] 加密后的数据 
     * */  
    public static byte[] encrypt(String data,String key) throws Exception{  
    	byte[] bytes = data.getBytes("UTF-8");//将数据转换成字节数组
    	
    	byte[] keyBytes = StreamUtil.base64ToByte(key);//还原密钥
    	DESedeKeySpec dks = new DESedeKeySpec(keyBytes);//实例化DES密钥
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);//实例化密钥工厂
        SecretKey sk = skf.generateSecret(dks);//生成密钥
        
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);//实例化  
        cipher.init(Cipher.ENCRYPT_MODE, sk);//初始化，设置为加密模式 
        return cipher.doFinal(bytes);//执行操作
    }
    
    /** 
     * 解密数据
     * @param data 待解密数据 
     * @param key 密钥 
     * @return String 解密后的数据 
     * */  
    public static String decrypt(byte[] data,String key) throws Exception{
    	byte[] keyBytes = StreamUtil.base64ToByte(key);//还原密钥
    	DESedeKeySpec dks = new DESedeKeySpec(keyBytes);//实例化DES密钥
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);//实例化密钥工厂
        SecretKey sk = skf.generateSecret(dks);//生成密钥

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);//实例化
        cipher.init(Cipher.DECRYPT_MODE, sk);//初始化，设置为解密模式 
        return new String(cipher.doFinal(data));//执行操作
    }
  
    
    
    public static void main(String[] args) throws Exception{
    	String symmetricKey = DESProvider.generateKey();
    	String data = "早上好";
    	byte[] ctext = DESProvider.encrypt(data, symmetricKey);
    	System.out.println(DESProvider.encrypt(data, symmetricKey));
    	String content = new String(ctext,"gbk");
    	System.out.println(content);
    	System.out.println(DESProvider.decrypt(ctext, symmetricKey));
    }
}