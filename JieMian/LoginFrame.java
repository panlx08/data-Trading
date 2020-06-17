package hfut.hu.BlockValueShare.JieMian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JDialog;
import javax.swing.border.EmptyBorder;
 
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.google.api.client.util.Strings;

import hfut.hu.BlockValueShare.blockbean.Wallet;
import hfut.hu.BlockValueShare.dao.LeveldbDAO;
import hfut.hu.BlockValueShare.sclient.MyClient3;
 
@SuppressWarnings("serial")
public class LoginFrame extends JDialog {
 
  private final JPanel contentPanel = new JPanel();
  private JTextField textField_1;
  private JTextField textField_2;
  private JTextField textField_3;
  private static LoginFrame dialog = null;
  public static String pubKey;
  public static String puId;
  public static String prKey;
  public static String prId;
  private static final int DIALOG_WIDTH=400;
  private static final int DIALOG_HEIGHT=340;
  private static final int DIALOG_HEIGHT_EXTEND=500;
  public static LeveldbDAO dao =new LeveldbDAO();
  /**
   * Create the dialog.
   */
  public LoginFrame() {
	setTitle("系统登陆");
    setAlwaysOnTop(true);
    setResizable(false);
    setBounds(800, 100, DIALOG_WIDTH,DIALOG_HEIGHT);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    contentPanel.setLayout(null);
 
    JButton register = new JButton("注 册");
    register.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if(LoginFrame.this.getHeight()==DIALOG_HEIGHT_EXTEND){
          LoginFrame.this.setSize(DIALOG_WIDTH,DIALOG_HEIGHT);
        }
        else{
          LoginFrame.this.setSize(DIALOG_WIDTH,DIALOG_HEIGHT_EXTEND);
        }
      }
    });
    register.setBounds(53, 210, 93, 23);
    contentPanel.add(register);
    JButton login = new JButton("登 录");
    login.setBounds(190, 210, 93, 23);
    contentPanel.add(login);
    login.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String find = dao.get(textField_1.getText());
			if (Strings.isNullOrEmpty(find)) {
				JOptionPane.showMessageDialog(contentPanel, "账户不存在", "ERROR", JOptionPane.ERROR_MESSAGE);
			}else {
				dialog.dispose();
				try {
					prId =textField_1.getText();
					puId = prId+"puk";
//					System.out.print(prId);
//					System.out.print(puId);
//					dao.tranverseAllDates();
					new MyClient3(dao,prId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	});
    textField_1 = new JTextField();
    textField_1.setBounds(123, 162, 150, 25);
    contentPanel.add(textField_1);
    textField_1.setColumns(10);
 
    JLabel lblNewLabel_1 = new JLabel("字符串");
    lblNewLabel_1.setBounds(80, 169, 54, 15);
    contentPanel.add(lblNewLabel_1);
 
    JLabel lblNewLabel_2 = new JLabel("image");   
    lblNewLabel_2.setBounds(0, 0, 360, 136);
    ImageIcon icon=new ImageIcon(LoginFrame.class.getResource("timg.jpg"));
    icon=ImageScale.getImage(icon, lblNewLabel_2.getWidth(), lblNewLabel_2.getHeight());
    lblNewLabel_2.setIcon((icon));
    contentPanel.add(lblNewLabel_2);
 
    JPanel panel = new JPanel();
    panel.setBorder(new TitledBorder(null, "注册用户", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    panel.setBounds(12, 259, 322, 131);
    contentPanel.add(panel);
    panel.setLayout(null);
 
    JLabel lblNewLabel_3 = new JLabel("字符串");
    lblNewLabel_3.setBounds(41, 29, 55, 18);
    panel.add(lblNewLabel_3);
 
    JLabel lblNewLabel_4 = new JLabel("确认");
    lblNewLabel_4.setBounds(41, 59, 55, 18);
    panel.add(lblNewLabel_4);
 
    textField_2 = new JTextField();
    textField_2.setBounds(123, 22, 150, 25);
    panel.add(textField_2);
    textField_2.setColumns(10);
 
    textField_3 = new JTextField();
    textField_3.setBounds(123, 52, 150, 25);
    panel.add(textField_3);
    textField_3.setColumns(10);
 
    JButton cancle = new JButton("取 消");
    cancle.setBounds(51, 89, 83, 27);
    panel.add(cancle);
    cancle.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            LoginFrame.this.setSize(DIALOG_WIDTH,DIALOG_HEIGHT);
        }
      });
    
    JButton confirm = new JButton("确 认");
    confirm.setBounds(190, 89, 83, 27);
    panel.add(confirm);
    contentPanel.add(login);
    confirm.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (textField_2.getText().equals(textField_3.getText())) {
	   	        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				Wallet walletB = new Wallet();
		        prId = textField_2.getText();
		        puId = prId+"puk";
		        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				 JFrame frame = new JFrame("账户信息");
			     frame.setBounds(800, 100, 600, 300);
			        JPanel fieldPanel = new JPanel();
			        fieldPanel.setBorder(new TitledBorder(null, "密钥信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			        fieldPanel.setLayout(null);
			        JLabel lblNewLabel_5 = new JLabel("公 钥");
			        lblNewLabel_5.setBounds(50, 50, 50, 20);
			        JLabel lblNewLabel_6 = new JLabel("私 钥");
			        lblNewLabel_6.setBounds(50, 95, 50, 20);
			        JLabel lblNewLabel_7 = new JLabel("请保存好您的密钥信息");
			        lblNewLabel_7.setFont(new Font(null, Font.PLAIN, 20));
			        lblNewLabel_7.setForeground(Color.DARK_GRAY);
			        lblNewLabel_7.setBounds(180, 140, 200, 20);
			        JTextArea txt1=new JTextArea(2,50);
			        txt1.setBounds(110, 50, 400, 30);
			        txt1.append(walletB.publicKey);
			        dao.put(puId, walletB.publicKey);
			        pubKey = walletB.publicKey;
			        JTextArea txt2=new JTextArea(2,50); 
			        txt2.append(walletB.privateKey);
			        dao.put(prId, walletB.privateKey);
//			        dao.tranverseAllDates();
			        prKey = walletB.privateKey;
			        txt2.setBounds(110, 90, 400, 30);
			        fieldPanel.add(txt1);
			        fieldPanel.add(txt2);
			        fieldPanel.add(lblNewLabel_5);
			        fieldPanel.add(lblNewLabel_6);
			        fieldPanel.add(lblNewLabel_7);
			     frame.add(fieldPanel);
			     frame.setVisible(true);
			     frame.setLocation(300,400);
			     frame.setAlwaysOnTop(true);
			}
			else {
				JOptionPane.showMessageDialog(panel, "密码不匹配", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	});
  }
  /**
   * Launch the application.
 * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
	  dao.init();
	      UIManager.put("RootPane.setupButtonVisible",false);
	      org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	      dialog = new LoginFrame();
	      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	      dialog.setVisible(true);
  }
}