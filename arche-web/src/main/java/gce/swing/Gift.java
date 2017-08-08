package gce.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Gift extends JFrame{

	private static final long serialVersionUID = 6872844652610569910L;
	private static final String name = "云在云上飘";
	//private LinkedList<JF> list = new LinkedList<JF>();
	
	 // JLabel 用作文本描述，还将它用作图片描述
	 private JLabel jLabel;
	 // 允许用户在 UI 中输入文本
	 private JTextField jTextField;  
	 private JButton jButton;
	 private JLabel hideLabel;
	 private Gift frame;
	 private int count = 0;
	 
	 String   strMsg1   =   "第一行，我是辛振球网页中插入多个空格间隔是需要特殊字符编码的ok";     
	 String   strMsg2   =   "第二行，她是莹莹网页中插入多个空格间隔是需要特殊字符编码的ok的的";     
	 String   strMsg = "<html><body>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+strMsg1+"<br>"+strMsg2+"<body></html>"; 
	 
	public Gift(){
		super();				
		frame = this;
		// JFrame的大小
		this.setSize(650, 200);
		// JFrame在屏幕居中
		this.setLocationRelativeTo(null);
		// 禁止改变JFrame的窗体大小
		this.setResizable(false);
		
	    // 设置布局管理器。FlowLayout \ GridLayout \ BorderLayout
		this.getContentPane().setLayout(null);
		
		this.add(getJLabel());  
		//this.add(getJTextField());  
		this.add(getJButton());	
		
		jLabel.setBounds(20, 25, this.getWidth() - 34, 45);
		
		jButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				count++;
				hideLabel.setText("" + count);
				changeContent(frame);
			}
			
		});
		// setContentPane(new ); 重新设置内容面板
		
		
		hideLabel = new JLabel();
		hideLabel.setBounds(0, 0 , 0, 0);
		
		// 当用户选择JFrame的关闭图标，将结束程序
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	       		
	}
	
	public static void main(String[] args) {
		setUIFont();
		Gift g = new Gift();
		
		g.setVisible(true);
	}
	
    public static void setUIFont()
    {
    	Font f = new Font("微软雅黑",Font.PLAIN,18);
    	String   names[]={ "Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
    			"JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
    			"TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
    			"OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
    			"ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
    			"PasswordField","TextField", "Table", "Label", "Viewport",
    			"RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
    	}; 
    	for (String item : names) {
    		 UIManager.put(item + ".font", f); 
    	}
    }	

	private javax.swing.JLabel getJLabel() {  
		   if(jLabel == null) {  
		      jLabel = new javax.swing.JLabel(strMsg);  
		      //jLabel.setBounds(34, 25, 150, 18);  
		      jLabel.setForeground(new Color(255, 0, 0));
		      
		   }  
		   return jLabel;  
		}  
		  
		private javax.swing.JTextField getJTextField() {  
		   if(jTextField == null) {  
		      jTextField = new javax.swing.JTextField();  
		      jTextField.setBounds(96, 49, 160, 20);
		      jTextField.setText("我是文本域");
		      jTextField.setForeground(Color.pink);
		   }  
		   return jTextField;  
		}  
		  
		private javax.swing.JButton getJButton() {  
		   if(jButton == null) {  
		      jButton = new javax.swing.JButton();  
		      jButton.setBounds(500, 110, 71, 30);  
		      jButton.setText("1月22");		      
		      // 改变1月22号的颜色
		      jButton.setForeground(new java.awt.Color(128, 128, 0));
		      // 改变jButton的背景颜色
		      jButton.setBackground(new java.awt.Color(17, 72, 232));
			  // 去掉按钮文字周围的焦点框
		      jButton.setFocusPainted(false);
			  // JButton的自适应文字的长度
		      jButton.setBorder(BorderFactory.createEtchedBorder());

		   }  
		   return jButton;  
		}  

		public JLabel getHideLabel() {
			return hideLabel;
		}

		public void setHideLabel(JLabel hideLabel) {
			this.hideLabel = hideLabel;
		}

		private void changeContent(Gift frame){
			int nFlag = Integer.parseInt(frame.getHideLabel().getText());
			
			switch(nFlag){
            default:
            	frame.getJButton();
            	frame.getJLabel();
            	frame.getJTextField();
                break;
            case 1:
            	frame.getJLabel().setText("在java1.7后支持了对string的判断");
            	frame.getJLabel().setBounds(34, 25, frame.getWidth() - 34, 45);
                break;
            case 2:
            	frame.getJLabel().setText("java switch语句就是一个多条件选择执行语句");
            	frame.getJLabel().setBounds(34, 25, frame.getWidth() - 34, 45);
                break;
            case 3:
            	frame.getJLabel().setText("只需要在new出Jframe");
            	frame.getJLabel().setBounds(34, 25, frame.getWidth() - 34, 45);
                break;
            case 4:
            	frame.getJLabel().setText("最新回答: 2015年07月15日 ");
            	frame.getJLabel().setBounds(34, 25, frame.getWidth() - 34, 45);
                break;
			}
		}
}
