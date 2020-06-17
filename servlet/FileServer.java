package hfut.hu.BlockValueShare.servlet;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import send.ParseDataUtil;


/**
 * @author peng
 *
 */
/**
 * @author peng
 *
 */
public class FileServer {
	private int port =5203;
	private ServerSocket serversocket;
    private ServerSocket fileServerSocket;
	private JFrame frame;
	private JPanel pane_button;
	private JButton sent_button;
	private JButton get_button;
	private File serversendFile;
	private String path ="I://DataTrading//fileServer";
    private PrintWriter server_out;
    private BufferedReader server_in;
	private JScrollPane pane_showWindow;
	private JSplitPane pane_center;
	private JTextArea area_showWindow;
	private Dimension dimension;//用于设置area_showWindow可拖拉的大小
	private String getfileName;
	private String getfileSize;
	private String sendPath = "I://DataTrading//fileServer";
	
	public FileServer() {
		frame = new JFrame();
		pane_button = new JPanel();
		pane_showWindow = new JScrollPane();
		area_showWindow = new JTextArea();
		pane_center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, pane_showWindow, null);
		dimension = new Dimension(50, 300);
		sent_button = new JButton("发送");
		get_button =  new JButton("接收");
		}
	
	public void connect() {
		try {
		 serversocket =  new ServerSocket(this.port); // 创建消息传输服务器
		 fileServerSocket = new ServerSocket(8888);  //创建文件传输服务器
		 while(true) {
             Socket client_socket = serversocket.accept();
             new ServerThread(client_socket).start();
             server_out.println("@action=ok");
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
	}
	
	public void SendFile(String s)
	{
		String sendFile = sendPath + s;
		@SuppressWarnings("unused")
		File serversendfile = new File(sendFile);
		if(serversendFile != null) 
            server_out.println("@action=Send["+serversendFile.getName()+":"+serversendFile.length()+":null]");
            area_showWindow.append("服务端发送："+serversendFile.getName()+"\r\n");	
	}
	
	@SuppressWarnings("unused")
	private void initVariable() throws Exception {
		frame.setTitle("服务端");
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setBounds(width / 2, height / 2, 400, 450);
		frame.setVisible(true);
		
		//取得视图焦点
		pane_showWindow.getViewport().add(area_showWindow);
		//pane_inputWindow.getViewport().add(area_inputWindow);
		//将显示文本域设置为不可编辑
		area_showWindow.setEditable(false);
		//设置显示文本域可拖拉的大小 
		pane_showWindow.setMinimumSize(dimension);
		frame.add(pane_center, BorderLayout.CENTER);
		
		pane_button.add(sent_button);
		pane_button.add(get_button);
		frame.add(pane_button, BorderLayout.SOUTH);
		sent_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser(); // 查找文件
                f.showOpenDialog(null);
                serversendFile = f.getSelectedFile();
                area_showWindow.append("服务端选择："+serversendFile.getPath()+"\r\n");
                if(serversendFile != null) 
                server_out.println("@action=Send["+serversendFile.getName()+":"+serversendFile.length()+":null]");
                area_showWindow.append("服务端发送："+serversendFile.getName()+"\r\n");
			}
		});
	}
	//服务端消息线程
	public class ServerThread extends Thread{
		private Socket client_socket;
        public ServerThread(Socket client_socket) {
            try {
                //初始化
                this.client_socket = client_socket;
                server_in = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
                server_out = new PrintWriter(this.client_socket.getOutputStream(),true);


            } catch (IOException e) {
            }
        }
	
        public void run() {
        	String message=null;
        	try {
				while((message=server_in.readLine())!=null){
					if (message.startsWith("@action=Upload")){
						getfileName = ParseDataUtil.getFileName(message);
                        getfileSize = ParseDataUtil.getFileSize(message);
                        File file = new File(path,getfileName);
                        //文件是否已存在
                        if (file.exists()){
                            //文件已经存在无需上传
                            server_out.println("@action=Upload[null:null:NO]");
                        }else {
                            //通知客户端开可以上传文件
                            server_out.println("@action=Upload["+getfileName+":"+getfileSize+":YES]");
                            //开启新线程上传文件
                            area_showWindow.append("客户端开始上传"+getfileName+"\r\n");
                            new HandleFileThread(1,getfileName,getfileSize).start();
                        }
					}
					
					if (message.startsWith("@action=Send")){
                        String res = ParseDataUtil.getResult(message);
                        if("NO".equals(res)){
                            JOptionPane.showMessageDialog(null,"文件已存在!");
                        }else if ("YES".equals(res)){
                            //开始上传
                            if(serversendFile != null){
                                //开启新线程传输文件
                                new HandleFileThread(0).start();
                            }

                        }else if ("上传完成".equals(res)){
                            //JOptionPane.showMessageDialog(null,res);
                            //loadGroupFile();
                        }
                    }
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	//文件传输线程
	public class HandleFileThread extends Thread{
		private int mode;  //文件传输模式  1-上传  2-下载
        private String filename;
        private Long fileSize;

        public HandleFileThread(int mode) {
            this.mode = mode;
        }
        
        public HandleFileThread(int mode,String filename,String fileSize){
            this.mode = mode;
            this.filename = filename;
            this.setFileSize(Long.parseLong(fileSize));
        }
        public void run() {
            try {
            	Socket socket = fileServerSocket.accept();
                //接收文件模式
                if(this.mode == 1){
                	//开始接收上传
                    BufferedInputStream file_in = new BufferedInputStream(socket.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path,filename)));

                    int len;
                    byte[] arr = new byte[8192];

                    while ((len = file_in.read(arr)) != -1){
                        bos.write(arr,0,len);
                        bos.flush();
                    }
                    server_out.println("@action=Upload[null:null:上传完成]");
                    server_out.println("\n");
                    area_showWindow.append("上传完成"+"\r\n");
                    bos.close();
                }

                //发送模式
                if(mode == 0){
                	@SuppressWarnings("resource")
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(serversendFile));
                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                    int len;
                    int i = 0;
                    double sum = 0;
                    byte[] arr = new byte[8192];
                    String schedule;

                    System.out.println("开始上传--文件大小为："+serversendFile.length());

                    while((len = bis.read(arr)) != -1){
                        bos.write(arr,0,len);
                        bos.flush();
                        sum += len;
                        if (i++ %100 == 0){
                            schedule = "上传进度:"+100*sum/serversendFile.length()+"%";
                            System.out.println(schedule);
                        }
                    }
                    //上传完成
                    socket.shutdownOutput();
                    System.out.println("上传进度:100%");
                 }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		public Long getFileSize() {
			return fileSize;
		}

		public void setFileSize(Long fileSize) {
			this.fileSize = fileSize;
		}
	}
	
	public static void main(String[] args) throws Exception {
		FileServer server = new FileServer();
//		server.initVariable();
		server.connect();
	}
}
