package hfut.hu.BlockValueShare.sclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import hfut.hu.BlockValueShare.blockbean.Key;
import hfut.hu.BlockValueShare.dh.RSAProvider;
import hfut.hu.BlockValueShare.dh.StreamUtil;

public class AgentThread extends Thread {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentThread.class);
	private Socket socket;
	String key;
    String res;
    String req;
    static String msg;
    static Gson gson= new Gson();
    /**
     * 默认构造函数
	 * @param socket
     */
	 public AgentThread(Socket socket){	
		 	this.socket = socket;
	   }

	 @Override
	 public void run(){
		 try {
			req = null;
            res = null;
			BufferedReader in = new BufferedReader
						(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);	
			String input;
			while((input = in.readLine()) != null) {
				 MyClient3.jta.append("接收消息 \n");
//				  req =input;
				 //处理接受
				String[] parts = input.split(" ");
				parts[0] = parts[0].toLowerCase();
				if ("send".equals(parts[0])) {
					 MyClient3.jta.append(parts[1]+"\n");
					 out.println("get");
				}
				if ("gei".equals(parts[0])) {
					 MyClient3.jta.append(parts[1]+"\n");
					 Key de = gson.fromJson(parts[1], Key.class);
					 byte[] text = StreamUtil.base64ToByte(de.getKey());
				   	 String kA = RSAProvider.decryptPrivateKey(text,MyClient3.prB); 
	 			   	 MyClient3.jta.append("得到密钥"+ kA+"\n");
	 			   	 out.println("get");
						}
				 
				  //处理发送
				  while (res == null){
                  	TimeUnit.MILLISECONDS.sleep(25);
                  }
				 out.println(res);
				 req = null;
                 res = null;
			}
			
		} catch (Exception e) {					
				LOGGER.info("The Deal has yet to go through");
		}
	   }
}
