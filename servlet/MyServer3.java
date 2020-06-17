package hfut.hu.BlockValueShare.servlet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.google.gson.Gson;

import hfut.hu.BlockValueShare.blockbean.Key;
import hfut.hu.BlockValueShare.blockbean.QianBao;
import hfut.hu.BlockValueShare.dh.StreamUtil;



	public class MyServer3 extends JFrame  implements ActionListener
	{
		//加密文件密钥
		String prA;
		static Key key = new Key();
		Gson gson = new Gson();
		//接收密钥
		public static String puB;
		public static QianBao B;
		private static final long serialVersionUID = 1L;
		private static ServerServer serverAgent;
		static JTextArea jta = null;
		JTextField jtf = null;
		JButton linkBtn = null;
		JButton sBtn = null;
		JButton keyBtn = null;
		JPanel jp = null;
		JScrollPane jsp = null;
		PrintWriter pw = null;
		private String string;
		
		public MyServer3() throws Exception
		{
			UIManager.put("RootPane.setupButtonVisible",false);
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
		    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//			String symmetricKey = DESProvider.generateKey();
//			StreamUtil.printToFile("A/SymmetricKey", symmetricKey);
			prA = StreamUtil.readFromFile("A/SymmetricKey");
			serverAgent = new ServerServer(30000);
			jta = new JTextArea();
			jtf = new JTextField(20);
			linkBtn = new JButton("同意");
			sBtn = new JButton("发送");
			sBtn.addActionListener(this);
			linkBtn.addActionListener(this);
			keyBtn = new JButton("发送密钥");
			keyBtn.addActionListener(this); 
			//监听回车
			jtf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {    
				if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作; 
			    { 
					sBtn.doClick();
			    } 
			} 
		});
			linkBtn.setActionCommand("同意");	
			keyBtn.setActionCommand("发送密钥");
			sBtn.setActionCommand("发送");
			jsp = new JScrollPane(jta);
			jp = new JPanel();
			jp.add(jtf);
			jp.add(sBtn);
			jp.add(linkBtn);	
			jp.add(keyBtn);
		
			this.add(jsp,BorderLayout.CENTER);
			this.add(jp,BorderLayout.SOUTH);
			this.setSize(600, 600);
			this.setTitle("数据平台服务端");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.setResizable(false);
			
			while(true) {
				try {
					for (ServerThread th:serverAgent.serverThreads) {
					if (string != null) {
					th.res = string;
					MyServer3.jta.append("已发送"+string+"\n");
					string=null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("发送密钥")) {
				//发送pk2加密的rk1
				jta.append("正在接收"+"\r\n");
				string= "gei "+ gson.toJson(key);
//				SendKey();
			}
			else if (e.getActionCommand().equals("发送")) {
				//发送pk1加密的k1
//				SendKey(); //发送同意
				string = "send "+jtf.getText();
			} 
			else if (e.getActionCommand().equals("同意")) {
				try {
					//建立SeverSOcket
					jta.append("已同意交易"+"\r\n");	
					serverAgent.start();
					jta.append("等待连接"+"\r\n");	
				} catch (Exception e1) {					
					e1.printStackTrace();
				}
				}

		}
	public static void SendKey() {
		//用pk2加密的私钥rk1
		//用pk1加密的k1
		try {
			for (ServerThread th:serverAgent.serverThreads) {
				String request = null; 
				th.res = request;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		
			new MyServer3();
		}
			
}