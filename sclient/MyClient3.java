package hfut.hu.BlockValueShare.sclient;

import java.io.*;
import java.net.*;
import java.security.Security;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hfut.hu.BlockValueShare.JieMian.LoginFrame;
import hfut.hu.BlockValueShare.blockbean.ChaZhaoBean;
import hfut.hu.BlockValueShare.blockbean.Key;
import hfut.hu.BlockValueShare.blockbean.PingJiaBean;
import hfut.hu.BlockValueShare.blockbean.QianBao;
import hfut.hu.BlockValueShare.blockbean.ShangChuan;
import hfut.hu.BlockValueShare.blockbean.SideBlock;
import hfut.hu.BlockValueShare.blockbean.Transaction;
import hfut.hu.BlockValueShare.blockbean.Wallet;
import hfut.hu.BlockValueShare.dao.LeveldbDAO;
import hfut.hu.BlockValueShare.dh.StreamUtil;
import hfut.hu.BlockValueShare.servlet.Node;
import hfut.hu.BlockValueShare.util.DeEnCoderHutoolUtil;
import send.Client;
import send.DecFile;
/**
	 * 主界面
	 */
public class MyClient3 extends JFrame implements ActionListener{
	static LeveldbDAO dao=new LeveldbDAO();
	public static String pkk;
	public static String prk;
	static Key exKey = new Key();
	public static String puB;
	public static String prB;
	private static final long serialVersionUID = 1L;
	final static Gson gson = new GsonBuilder().create();
	final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

	public static QianBao A;
	public static String ip ="192.168.43.203" ;
	static JTextArea jta = null;
	JScrollPane jsp = null;
	static PrintWriter pw = null;
	PrintWriter n = null;
	static AgentThread agents;
	static Wallet wallet;
	static String msg = DeEnCoderHutoolUtil.rsaEncrypt("这里是测试数据");	
	public MyClient3(LeveldbDAO dao,String prId) throws Exception
	{
//		Map<String, Object> keyPair = RSAProvider.generateKeyPair();//生成密钥对
//		String pubkey = RSAProvider.getPublicKeyBytes(keyPair);
//		StreamUtil.printToFile("B/BPublicKey", pubkey);
//		String prikey = RSAProvider.getPrivateKeyBytes(keyPair);
//		StreamUtil.printToFile("B/BPrivateKey", prikey);
		puB = StreamUtil.readFromFile("B/BPublicKey");
		prB = StreamUtil.readFromFile("B/BPrivateKey");
		MyClient3.dao = dao;
		UIManager.put("RootPane.setupButtonVisible",false);
		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
	    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    pkk = dao.get(prId+"puk");
	    prk = dao.get(prId);
		System.out.println("1"+prId);
	    dao.tranverseAllDates();
	    System.out.println(pkk);
		System.out.println(prk);
		// 创建选项卡面板
        final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,
        		JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);//设置选项卡标签的布局方式为滚动布局
        tabbedPane.addTab("需求发布", createTextPanel1());
        tabbedPane.addTab("注册数据", createTextPanel2());
        tabbedPane.addTab("交易", createTextPanel3());
        tabbedPane.addTab("评价", createTextPanel4());
        tabbedPane.addTab("交流", createTextPanel5());
//        // 设置默认选中的选项卡
//        tabbedPane.setSelectedIndex(1);
			jta = new JTextArea();
	        jsp = new JScrollPane(jta);
			JFrame hhh = new JFrame("hi");
			hhh.setLayout(new BorderLayout());
			hhh.setBounds(550, 100, 950, 900);
			hhh.setTitle("大数据交易平台");
			hhh.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			hhh.setVisible(true);
			hhh.setResizable(false);
		JPanel centerPane = new JPanel();
		centerPane.setSize(950, 900);
        hhh.add(centerPane);
        centerPane.setLayout(new GridBagLayout());
        final GridBagConstraints bag1 = new GridBagConstraints();
        jta.setForeground(Color.darkGray);
        bag1.gridy = 0;// 起始点为第1行  
        bag1.gridx = 2;// 起始点为第3列  
        bag1.gridheight = 2;// 组件占用两行  
        bag1.insets = new Insets(0, 5, 0, 0);  
        bag1.weightx = 30;// 第一列的分布方式为30%  
        bag1.weighty = 90;// 第一列的分布方式为10%  
        bag1.fill = GridBagConstraints.BOTH;// 同时调整组件的宽度和高度 
        centerPane.add(jsp, bag1);  
        final GridBagConstraints bag2 = new GridBagConstraints();  
        bag2.gridy = 0;  
        bag2.gridx = 3;  
        bag2.gridheight = 4;  
        bag2.insets = new Insets(0, 5, 0, 5);// 设置组件左侧和右侧的最小距离  
        bag2.weightx = 50;// 第一列的分布方式为40% 
        bag2.fill = GridBagConstraints.BOTH;  
        centerPane.add(tabbedPane, bag2);  
        final GridBagConstraints bag3 = new GridBagConstraints();  
        bag3.gridy = 2;  
        bag3.gridx = 2;  
        bag3.gridwidth = 1;// 组件占用两列  
        bag3.gridheight = 1;// 组件占用两行
        bag3.weighty = 10;// 第一列的分布方式为10%  
        bag3.insets = new Insets(5, 5, 0, 0);  
        bag3.fill = GridBagConstraints.BOTH;// 只调整组件的高度   		
        JPanel jPanel = new JPanel();
        JButton btn1=new JButton("查看数据链");
        btn1.setPreferredSize(new Dimension(100,60));
        btn1.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			String info = "getinfo";
    			jta.setText("");
    			//把客户端发送的信息显示到jta
    			jta.append("client  "+getTime()+"\r\n"+info+"\r\n");
    			pw.println(info);
    		}
    	});
        JButton btn2=new JButton("查看交易链");
        btn2.setPreferredSize(new Dimension(100,60));
        btn2.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			String info = "getinfo2";
    			jta.setText("");
    			//把客户端发送的信息显示到jta
    			jta.append("client  "+getTime()+"\r\n"+info+"\r\n");
    			pw.println(info);
    		}
    	});
        JPanel a = new JPanel();
        a.add(btn1);
        JPanel b = new JPanel();
        a.add(btn2);
        jPanel.setLayout(new GridLayout(1,2,10,5));
        jPanel.add(a);
        jPanel.add(b);
        centerPane.add(jPanel, bag3);  
		try {
			@SuppressWarnings("resource")
			Socket s = new Socket("127.0.0.1",8016);
			BufferedReader br = new BufferedReader
					(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream(),true);

			while(true) {
				//不停的读取服务器端发来的信息
				String info = br.readLine();
				jta.append(info+"\r\n");
			}
		} catch (Exception e) {			
		}
	}
	public static void main(String[] args) throws Exception {
		dao.init();
		new MyClient3(dao,"pry");
		}
	 public static SideBlock generateBlock(QianBao walletA, String to,float value) throws Exception {
	    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());    	
	        SideBlock newBlock = new SideBlock("待设置");
	        newBlock.addTransaction(Node.UTXOs,walletA.sendFunds(Node.UTXOs,to, value));
			return newBlock;
	    }
	//获取系统时间
	public static String getTime() 
	{
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		int second = c.get(Calendar.SECOND);
		return hour+":"+ minute+":"+second;
	}
	/**
	 * 
	 * @return 查看
	 */
	private static JPanel createTextPanel1() {
		JPanel contentPanel = new JPanel();
		JTextField find;
    	JTextField type;
    	JTextField num;
    	JTextField pubKey;
    	JTextField deadline;
    	JTextField qlimit;
    	
    	
    	contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPanel.setLayout(null);
     
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "数据查阅", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(12, 59, 306, 250);
        contentPanel.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("数据类型");
        lblNewLabel_3.setBounds(10, 50, 80, 18);
        panel.add(lblNewLabel_3);
     
        JLabel lblNewLabel_4 = new JLabel("需求数量");
        lblNewLabel_4.setBounds(10, 85, 80, 18);
        panel.add(lblNewLabel_4);
     
        JLabel lblNewLabel_5 = new JLabel("您的公钥");
        lblNewLabel_5.setBounds(10, 115, 80, 18);
        panel.add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("期限");
        lblNewLabel_6.setBounds(10, 145, 80, 18);
        panel.add(lblNewLabel_6);
        
        JLabel lblNewLabel_7 = new JLabel("质量要求");
        lblNewLabel_7.setBounds(10, 175, 80, 18);
        panel.add(lblNewLabel_7);
     
        type = new JTextField("usual");
        type.setBounds(100, 48, 150, 25);
        panel.add(type);
        type.setColumns(10);
     
        num = new JTextField("1");
        num.setBounds(100, 80, 150, 25);
        panel.add(num);
        num.setColumns(10);
     
        pubKey = new JTextField(pkk);
        pubKey.setBounds(100, 113, 150, 25);
        panel.add(pubKey);
        pubKey.setColumns(10);
        
        deadline = new JTextField("2000");
        deadline.setBounds(100, 143, 150, 25);
        panel.add(deadline);
        deadline.setColumns(10);
        
        qlimit = new JTextField("10");
        qlimit.setBounds(100, 173, 150, 25);
        panel.add(qlimit);
        qlimit.setColumns(10);
     
        JButton confirm = new JButton("查找");
        confirm.setBounds(120, 205, 83, 27);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					ChaZhaoBean bean = new ChaZhaoBean();
					bean.setType(type.getText().replace(" ",""));
					bean.setNum(Integer.parseInt(num.getText()));
					bean.setPublicKey(pubKey.getText());
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("mama "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        
        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(null, "区块查询", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel2.setBounds(12, 359, 306, 221);
        contentPanel.add(panel2);
        panel2.setLayout(null);
        
        JLabel lblNewLabel_8 = new JLabel("区块序号");
        lblNewLabel_8.setBounds(10, 85, 55, 18);
        panel2.add(lblNewLabel_8);
        
        find = new JTextField("1");
        find.setBounds(83, 80, 150, 25);
        panel2.add(find);
        num.setColumns(10);
        JButton confirm2 = new JButton("查找");
        confirm2.setBounds(120, 132, 83, 27);
        panel2.add(confirm2);
      //监听回车
        find.addKeyListener(new KeyAdapter() {
      			public void keyPressed(KeyEvent e)    
      		      {    
      		        if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作; 
      		        { 
      		        	confirm2.doClick();
      		        } 
      		      } 
      		});
        confirm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					ChaZhaoBean bean = new ChaZhaoBean();
					bean.setNum(Integer.parseInt(find.getText()));
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("find "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        return contentPanel;
    }
	/**
	 * 
	 * @return 上传
	 * @throws UnknownHostException 
	 */
	private static JPanel createTextPanel2() throws UnknownHostException {
		JPanel contentPanel = new JPanel();
		JTextField num;
    	JTextField type;
    	JTextField pubKey;
    	JTextField cost;
    	JTextField content;
    	JTextField sp;
    	JTextField uid;
    	JTextField sign;
    	JTextField hash;
    	
    	
    	contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPanel.setLayout(null);
     
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "数据注册", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(12, 59, 306, 400);
        contentPanel.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("数据数量");
        lblNewLabel_3.setBounds(10, 50, 80, 18);
        panel.add(lblNewLabel_3);
     
        JLabel lblNewLabel_4 = new JLabel("数据类型");
        lblNewLabel_4.setBounds(10, 85, 80, 18);
        panel.add(lblNewLabel_4);
     
        JLabel lblNewLabel_5 = new JLabel("交易账户");
        lblNewLabel_5.setBounds(10, 115, 80, 18);
        panel.add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("数据描述");
        lblNewLabel_6.setBounds(10, 148, 80, 18);
        panel.add(lblNewLabel_6);
     
        JLabel lblNewLabel_7 = new JLabel("数据价格");
        lblNewLabel_7.setBounds(10, 180, 80, 18);
        panel.add(lblNewLabel_7);
        
        JLabel lblNewLabel_8 = new JLabel("存储证明");
        lblNewLabel_8.setBounds(10, 210, 80, 18);
        panel.add(lblNewLabel_8);
        
        JLabel lblNewLabel_9 = new JLabel("数据UID");
        lblNewLabel_9.setBounds(10, 240, 80, 18);
        panel.add(lblNewLabel_9);
        
        JLabel lblNewLabel_10 = new JLabel("提供商签名");
        lblNewLabel_10.setBounds(10, 270, 80, 18);
        panel.add(lblNewLabel_10);
        
        JLabel lblNewLabel_11 = new JLabel("数据哈希值");
        lblNewLabel_11.setBounds(10, 300, 80, 18);
        panel.add(lblNewLabel_11);
        
        num = new JTextField("1");
        num.setBounds(83, 48, 150, 25);
        panel.add(num);
        num.setColumns(10);
        
        type = new JTextField("usual");
        type.setBounds(83, 80, 150, 25);
        panel.add(type);
        type.setColumns(10);
        
        pubKey = new JTextField(pkk);
        pubKey.setBounds(83, 113, 150, 25);
        panel.add(pubKey);
        pubKey.setColumns(10);
     
        content = new JTextField("hfut");
        content.setBounds(83, 145, 150, 25);
        panel.add(content);
        content.setColumns(10);
        
        cost = new JTextField("1");
        cost.setBounds(83, 177, 150, 25);
        panel.add(cost);
        cost.setColumns(10);
        
        sp = new JTextField("server 1");
        sp.setBounds(83, 207, 150, 25);
        panel.add(sp);
        sp.setColumns(10);
        
        uid = new JTextField("1");
        uid.setBounds(83, 237, 150, 25);
        panel.add(uid);
        uid.setColumns(10);
        
        sign = new JTextField("1");
        sign.setBounds(83, 267, 150, 25);
        panel.add(sign);
        sign.setColumns(10);
        
        hash = new JTextField("1");
        hash.setBounds(83, 297, 150, 25);
        panel.add(hash);
        hash.setColumns(10);
        
        JButton confirm2 = new JButton("上传文件");
        confirm2.setBounds(80, 342, 83, 27);
        
        panel.add(confirm2);
        confirm2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Client client = new Client();
					client.initVariable();
////					client.connectServer("127.0.0.1");
					client.connectServer("127.0.0.1");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
        JButton confirm = new JButton("上传");
        confirm.setBounds(140, 342, 83, 27);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					ShangChuan bean = new ShangChuan();
					bean.setType(type.getText().replace(" ",""));
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add(content.getText().replace(" ",""));
					bean.setDataSummary(arrayList);
					ArrayList<String> arrayList1 = new ArrayList<String>();
					arrayList1.add(sp.getText().replace(" ",""));
					bean.setStorageProof(arrayList1);
					ArrayList<String> arrayList2 = new ArrayList<String>();
					arrayList2.add(uid.getText().replace(" ",""));
					bean.setDataUid(arrayList2);
					ArrayList<String> arrayList3 = new ArrayList<String>();
					arrayList3.add(sign.getText().replace(" ",""));
					bean.setDataSign(arrayList3);
					ArrayList<String> arrayList4 = new ArrayList<String>();
					arrayList4.add(hash.getText().replace(" ",""));
					bean.setDataHash(arrayList4);
					bean.setCost(Integer.parseInt(cost.getText()));
					bean.setNum(Integer.parseInt(num.getText()));
					bean.setPubKey(pubKey.getText());
					bean.setIp(InetAddress.getLocalHost().getHostAddress());
					String text = gson.toJson(bean);
//					jta.append(text+"\n");
					pw.println("sends "+text);
				} catch (NumberFormatException | UnknownHostException e1) {
					e1.printStackTrace();
				}
			}
		});
        return contentPanel;
    }
	/**
	 * 
	 * @return 交易
	 */
	private static JPanel createTextPanel3() {
    	JPanel contentPanel = new JPanel();
    	JTextField value;
    	JTextField to;
    	JTextField from;
    	JTextField find;
    	contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPanel.setLayout(null);
     
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "进行交易", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(12, 59, 306, 311);
        contentPanel.add(panel);
        panel.setLayout(null);
        
     
        JLabel lblNewLabel_4 = new JLabel("交易金额");
        lblNewLabel_4.setBounds(10, 85, 55, 18);
        panel.add(lblNewLabel_4);
     
        JLabel lblNewLabel_5 = new JLabel("对方公钥");
        lblNewLabel_5.setBounds(10, 115, 55, 18);
        panel.add(lblNewLabel_5);
     
        JLabel label = new JLabel("本方公钥");
        label.setBounds(10, 145, 55, 18);
        panel.add(label);
     
        value = new JTextField("0");
        value.setBounds(83, 80, 150, 25);
        panel.add(value);
        value.setColumns(10);
     
        to = new JTextField("xxxxxxxxx");
        to.setBounds(83, 113, 150, 25);
        panel.add(to);
        to.setColumns(10);
     
        from = new JTextField(pkk);
        from.setBounds(83, 145, 150, 25);
        panel.add(from);
        from.setColumns(10);
         
        JButton confirm = new JButton("确认交易");
        confirm.setBounds(120, 182, 83, 27);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					Transaction bean = new Transaction();
					bean.setSender(from.getText().replace(" ",""));
					bean.setReciepient(to.getText().replace(" ",""));
					bean.setValue(Integer.parseInt(value.getText()));
					bean.setSignature(prk);
					System.out.print(bean.getSignature());
					String text = gson.toJson(bean);
					dao.close();
					jta.append(text+"\n");
					pw.println("tran "+text);
				} catch (NumberFormatException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
        JButton confirm2 = new JButton("查询余额");
        confirm2.setBounds(120, 222, 83, 27);
        panel.add(confirm2);
        confirm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Transaction bean = new Transaction();
					bean.setSender(from.getText().replace(" ",""));
					bean.setSignature(dao.get(LoginFrame.prId));
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("wallet "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        
        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(null, "区块查询", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel2.setBounds(12, 389, 306, 221);
        contentPanel.add(panel2);
        panel2.setLayout(null);
        
        JLabel lblNewLabel_6 = new JLabel("公钥查询");
        lblNewLabel_6.setBounds(10, 85, 55, 18);
        panel2.add(lblNewLabel_6);
        
        find = new JTextField(pkk);
        find.setBounds(83, 80, 150, 25);
        panel2.add(find);
        find.setColumns(10);
        
        JButton confirm21 = new JButton("查找");
        confirm21.setBounds(120, 132, 83, 27);
        panel2.add(confirm21);
      //监听回车
        find.addKeyListener(new KeyAdapter() {
      			public void keyPressed(KeyEvent e)    
      		      {    
      		        if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作; 
      		        { 
      		        	confirm21.doClick();
      		        } 
      		      } 
      		});
        confirm21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					ChaZhaoBean bean = new ChaZhaoBean();
					bean.setPublicKey(find.getText());
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("finds "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        return contentPanel;
    }
	/**
	 * 
	 * @return 评价
	 */
	private static JPanel createTextPanel4() {
    	JPanel contentPanel = new JPanel();
    	JTextField xuHao;
    	JTextField fenShu;
    	JTextField ping1;
    	JTextField ping2;
    	contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        contentPanel.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "用户评价", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(12, 59, 306, 221);
        contentPanel.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("数据序号");
        lblNewLabel_3.setBounds(10, 50, 55, 18);
        panel.add(lblNewLabel_3);
     
        JLabel lblNewLabel_4 = new JLabel("分数");
        lblNewLabel_4.setBounds(10, 85, 55, 18);
        panel.add(lblNewLabel_4);
     
        JLabel lblNewLabel_5 = new JLabel("使用评价1");
        lblNewLabel_5.setBounds(10, 115, 55, 18);
        panel.add(lblNewLabel_5);
     
        JLabel label = new JLabel("使用评价2");
        label.setBounds(10, 145, 55, 18);
        panel.add(label);
     
        xuHao = new JTextField("1");
        xuHao.setBounds(83, 48, 150, 25);
        panel.add(xuHao);
        xuHao.setColumns(10);
     
        fenShu = new JTextField("10");
        fenShu.setBounds(83, 80, 150, 25);
        panel.add(fenShu);
        fenShu.setColumns(10);
     
        ping1 = new JTextField("选填");
        ping1.setBounds(83, 113, 150, 25);
        panel.add(ping1);
        ping1.setColumns(10);
     
        ping2 = new JTextField("选填");
        ping2.setBounds(83, 145, 150, 25);
        panel.add(ping2);
        ping2.setColumns(10);
         
        JButton confirm = new JButton("确认评价");
        confirm.setBounds(120, 182, 83, 27);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					PingJiaBean bean = new PingJiaBean();
					bean.setEvaluate(ping1.getText().replace(" ",""));
					bean.setFind(Integer.parseInt(xuHao.getText()));
					bean.setScore(Integer.parseInt(fenShu.getText()));	
					bean.setEvaluate2(ping2.getText().replace(" ",""));
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("baba "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(null, "区块查询", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel2.setBounds(12, 359, 306, 221);
        contentPanel.add(panel2);
        panel2.setLayout(null);
        
        JLabel lblNewLabel_6 = new JLabel("区块序号");
        lblNewLabel_6.setBounds(10, 85, 55, 18);
        panel2.add(lblNewLabel_6);
        
        JTextField find = new JTextField("1");
        find.setBounds(83, 80, 150, 25);
        panel2.add(find);
        
        JButton confirm2 = new JButton("查找");
        confirm2.setBounds(120, 132, 83, 27);
        panel2.add(confirm2);
      //监听回车
        find.addKeyListener(new KeyAdapter() {
      			public void keyPressed(KeyEvent e)    
      		      {    
      		        if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作; 
      		        { 
      		        	confirm2.doClick();
      		        } 
      		      } 
      		});
        confirm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jta.setText("");
					ChaZhaoBean bean = new ChaZhaoBean();
					bean.setNum(Integer.parseInt(find.getText()));
					String text = gson.toJson(bean);
					jta.append(text+"\n");
					pw.println("find "+text);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		});
        return contentPanel;
    }
	private static JPanel createTextPanel5() {
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.setBorder(new EmptyBorder(50, 50, 50, 50));
		jp.setLayout(null);
		
		JTextField jtf = new JTextField(20);
		JButton keyBtn = new JButton("获取密钥");
		JButton faSong = new JButton("发送");
		
		JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "建立连接", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(12, 59, 306, 160);
        jp.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("IP地址");
        lblNewLabel_3.setBounds(30, 50, 55, 18);
        panel.add(lblNewLabel_3);
        
        JTextField ipAd = new JTextField("127.0.0.1");
        ipAd.setBounds(93, 48, 150, 25);
        panel.add(ipAd);
        ipAd.setColumns(10);
        
        JButton getLink = new JButton("建立连接");
        getLink.setBounds(100, 92, 83, 27);
        panel.add(getLink);
        
		JPanel panel2 = new JPanel();
	    panel2.setBorder(new TitledBorder(null, "买卖交流", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panel2.setBounds(12, 250, 306, 151);
	    jp.add(panel2);
	    panel2.setLayout(null);
		
	    jtf.setBounds(53, 48, 190, 25);
        panel2.add(jtf);
        jtf.setColumns(20);
        
        faSong.setBounds(153, 90, 70, 25);
        panel2.add(faSong);
        keyBtn.setBounds(73, 90, 70, 25);
        panel2.add(keyBtn);
		//监听回车
		jtf.addKeyListener(new KeyAdapter() {
      			public void keyPressed(KeyEvent e)    
      		      {    
      		        if(e.getKeyChar()==KeyEvent.VK_ENTER )   //按回车键执行相应操作; 
      		        { 
      		        	faSong.doClick();
      		        } 
      		      } 
      		});
		getLink.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			Socket server;
				try {
					server = new Socket(ipAd.getText(),30000);
					//建立Socket连接，并发送pk2
					agents = new AgentThread(server);	
					agents.start();	
					jta.append("可能已建立连接"+"\r\n");
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		}
    	});
		faSong.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			//接收pk2加密的rk1
				//接收pk1加密的k1
//				Send(pubKey2);	
    			agents.res = "msg "+ jtf.getText();
    			jta.append("已发送 " +jtf.getText()+"\r\n");
    		}
    	});
		keyBtn.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			try {
					puB = StreamUtil.readFromFile("B/BPublicKey");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			exKey.setKey(puB);
    			agents.res = "key "+ gson.toJson(exKey);
    			jta.append("已发送 " +agents.res+"\r\n");
    		}
    	});
		JPanel panel3 = new JPanel();
	    panel3.setBorder(new TitledBorder(null, "获取文件", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    panel3.setBounds(12, 450, 306, 81);
	    jp.add(panel3);
	    panel3.setLayout(null);
	    JButton jie = new JButton("解密");
	    jie.setBounds(120, 22, 83, 27);
        panel3.add(jie);
        jie.addActionListener(new ActionListener() {
  		@Override
  		public void actionPerformed(ActionEvent e) {	
  			DecFile decfile;
			try {
				decfile = new DecFile();
				decfile.initVariable();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
  			
  		}
  	});
        return jp;
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}