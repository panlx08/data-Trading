package hfut.hu.BlockValueShare.servlet;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import hfut.hu.BlockValueShare.blockbean.Key;
import hfut.hu.BlockValueShare.dh.RSAProvider;
import hfut.hu.BlockValueShare.dh.StreamUtil;
/**
 * 处理单个rpc连接
 */
public class ServerThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerThread.class);
    private Socket socket;
    public String res;
    String req;
    static String msg;
    static Gson gson= new Gson();
    static Key key = new Key();
    /**
     * 默认构造函数
     * @param socket
     */
    public ServerThread(Socket socket){
        this.socket = socket;
    }
	@Override
    public void run(){
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("send Server：已建立连接");
            String input;
            while((input = in.readLine()) != null){
            	 String[] parts =  input.split(" ");
            	 parts[0] = parts[0].toLowerCase();
            	 if ("msg".equalsIgnoreCase(parts[0])){
            		 MyServer3.jta.append(parts[1]+"\n");
            		 out.println(res);
            	 }else if ("key".equalsIgnoreCase(parts[0])) {
            		 MyServer3.jta.append(parts[1]+"\n");
            		 Key ex = gson.fromJson(parts[1], Key.class);
            		 MyServer3.puB = ex.getKey();
            		 String secretKey = StreamUtil.readFromFile("A/SymmetricKey");
            		 byte[] ctext = RSAProvider.encryptPublicKey(secretKey,ex.getKey());
            		 String jiami = StreamUtil.byteToBase64(ctext);
//            		 StreamUtil.printToFile("SecretKey", jiami);
            		 key.setKey(jiami);
            		 MyServer3.key = key;
            		 out.println("等待确认");
				}
//            	 else {
//            		 req = input;
//                     out.println(res);
//                     MyServer3.jta.append("已发送"+res);
//                }
               req = null;
               res = null;
            }
        } catch (Exception e){
            LOGGER.info("An CLient client has disconnected.");
        }
    }
}
