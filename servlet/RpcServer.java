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
public class RpcServer extends Thread
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    private static int port;
    private boolean runFlag = true;

    List<RpcThread> rpcThreads;
    /**
     * 默认配置
     */
    public RpcServer()
    {
        RpcServer.port = 8016;
        this.rpcThreads = new ArrayList<RpcThread>();
    }
  
    /**
     * 指定端口
     * @param port Port to listen on
     */
    public RpcServer(int port)
    {
        RpcServer.port = port;
        this.rpcThreads = new ArrayList<RpcThread>();
    }

    @Override
    public void run()
    {
        try
        {
            final ServerSocket socket = new ServerSocket(port);
            while (runFlag)
            {
            	//等待客户端连接
            	RpcThread thread = new RpcThread(socket.accept());
                rpcThreads.add(thread);
                thread.start();
            }
            socket.close();
        } catch (Exception e){
        	LOGGER.error("rpc error in port:" + port,e);
        }
    }
    
}