package hfut.hu.BlockValueShare.servlet;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC服务
 *
 * 注意：不要把这个端口开放给外网
 * @author Mignet
 */
public class ServerServer extends Thread
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerServer.class);
    private static int port;
    public List<ServerThread> serverThreads;
    /**
     * 默认配置
     */
    public ServerServer()
    {
        ServerServer.port = 30000;
        this.serverThreads = new ArrayList<ServerThread>();
    }
  
    /**
     * 指定端口
     * @param port Port to listen on
     */
    public ServerServer(int port)
    {
        ServerServer.port = port;
        this.serverThreads = new ArrayList<ServerThread>();
    }

    @SuppressWarnings("resource")
	@Override
    public void run()
    {
        try
        {
            final ServerSocket socket = new ServerSocket(port);
            while (true)
            {
            	//等待客户端连接
            	ServerThread thread = new ServerThread(socket.accept()); 
                serverThreads.add(thread);
                MyServer3.jta.append("取得连接"+"\n");
                thread.start();
            }
        } catch (Exception e){
        	LOGGER.error("rpc error in port:" + port,e);
        }
    }
    
}