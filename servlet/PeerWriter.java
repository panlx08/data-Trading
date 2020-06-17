package hfut.hu.BlockValueShare.servlet;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OutputThread 负责写数据到peer
 * @author Mignet
 */
public class PeerWriter extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(PeerWriter.class);

	private Socket socket;
	private ArrayList<String> outputBuffer;
	private boolean runFlag = true;

	public PeerWriter(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			outputBuffer = new ArrayList<String>();
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (runFlag) {
				if (!outputBuffer.isEmpty() && outputBuffer.get(0) != null) {
                    for (String line : outputBuffer) {
                    	LOGGER.info("Sending " +line + " to " + socket.getInetAddress());
                        out.println(line);
                    }
                    outputBuffer = new ArrayList<String>();
                    outputBuffer.add(null);
                }
				outputBuffer = new ArrayList<String>();
				outputBuffer.add(null);
				TimeUnit.MILLISECONDS.sleep(200);
			}
		} catch (Exception e) {
			 LOGGER.info("Peer " + socket.getInetAddress() + " disconnected."+e); 
		}
	}

	/**
	 * 写入缓冲
	 *
	 * @param data Data to write
	 */
	public void write(String data) {
		if (!outputBuffer.isEmpty()) {
			if (outputBuffer.get(0) == null) {
				outputBuffer.remove(0);
			}
		}
		outputBuffer.add(data);
	}

	/**
	 * maybe
	 */
	public void shutdown() {
		runFlag = false;
	}
}