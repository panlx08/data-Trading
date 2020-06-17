package hfut.hu.BlockValueShare.servlet;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 处理单个rpc连接
 */
public class RpcThread extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcThread.class);
	
    private Socket socket;
    String res;
    String req;

    /**
     * 默认构造函数
     * @param socket
     */
    public RpcThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            req = null;
            res = null;
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            out.println("[   Welcome RPC Daemon    ]");
            
            while((input = in.readLine()) != null){
                if ("HELP".equalsIgnoreCase(input)){
                	out.println("############################################# COMMANDS ############################################");
                    out.println("#     1) getinfo        		     - Gets block chain infomations.                               #");
                    out.println("#     2) send num type content      - Write to blockChain                                         #");
                    out.println("#     3) evaluate id evalu 		 - Give the block evaluation                                   #");
                    out.println("#     4) secret      				 - Send the publicKey                                		   #");
                    out.println("#     5) wallet      				 - Get a wallet                                 			   #");
                    out.println("#     6) data type num     		 - Find the data you need                                      #");
                    out.println("#######################################################################################################");
                } else {
                    req = input;
                    while (res == null){
                    	TimeUnit.MILLISECONDS.sleep(25);
                    }
                    out.println(res);
                    req = null;
                    res = null;
                }
            }
        } catch (Exception e){
            LOGGER.info("An RPC client has disconnected.");
        }
    }
    
}
