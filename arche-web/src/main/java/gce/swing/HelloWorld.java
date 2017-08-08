package gce.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

// JFrame类就是做这个的——它是一个容器，允许您把其他组件添加到它里面，把它们组织起来，并把它们呈现给用户。
public class HelloWorld extends JFrame{

	 // JLabel 用作文本描述，还将它用作图片描述
	 private JLabel jLabel;
	 // 允许用户在 UI 中输入文本
	 private JTextField jTextField;  
	 private JButton jButton;
	 
	 // 获取当前屏幕的分辨率
	 private static double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	 private static double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	   
	public HelloWorld()  
	{  
	   super("frame的标题");  	   
	   // 设置frame在屏幕中间显示
//	   putToCenter(this,300, 200);
	   // 参数类型是int,注意是以像素为单位
	   this.setSize(650, 200);
       // JFrame在屏幕居中
	   // 设置窗口相对于指定组件的位置。如果 c 为 null，则此窗口将置于屏幕的中央。
       this.setLocationRelativeTo(null);
       // JFrame关闭时的操作
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // 设置布局管理器 FlowLayout \ GridLayout \ BorderLayout
	   this.getContentPane().setLayout(null);

		// 获得与JFrame关联的contentPane
		// contentPane默认的布局管理器为BorderLayout
//		final Container contentPane = getContentPane();
//		// 取消布局管理器，改为手动布局
//		contentPane.setLayout(null);		
//		contentPane.add(jf.getButton());
//		contentPane.add(jf.getLabel());
	   
	   
	   // 将组件添加到frame中
	   this.add(getJLabel(), null);  
	   this.add(getJTextField(), null);  
	   this.add(getJButton());
	   // 设置frame的标题
	   this.setTitle("HelloWorld");   
        
       
       this.toFront();  
	     
	   
	}  
	
	private javax.swing.JLabel getJLabel() {  
	   if(jLabel == null) {  
	      jLabel = new javax.swing.JLabel("我是");  
	      jLabel.setBounds(34, 49, 53, 18);  
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
	      jButton.setBounds(103, 110, 71, 27);  
	      jButton.setText("1月22");
	      //JOptionPane.showConfirmDialog(jButton, "你高兴吗?", "标题",JOptionPane.YES_NO_OPTION);
	      // 改变1月22号的颜色
	      jButton.setForeground(new java.awt.Color(128, 128, 0));
	      jButton.setBackground(Color.blue);
	      // 添加鼠标监听事件
	      jButton.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                System.out.println("按钮被点击");
	                Object source = e.getSource();
	                JButton button = (JButton) source;
	                String text = button.getText();
	                if ("按钮被点击".equals(text)) {
	                    button.setText("点我");
	                } else {
	                    button.setText("按钮被点击");
	                }
	            }
	        });
	   }  
	   return jButton;  
	}  
	  
	  
	public static void main(String[] args)  
	{  
	   setUIFont();
	   HelloWorld w = new HelloWorld();  
	   w.setVisible(true);
	   int n = JOptionPane.showConfirmDialog(null, "你高兴吗?", "标题",JOptionPane.YES_NO_OPTION);//i=0/1 对应是与否
	   // JOptionPane.showMessageDialog(null, "提示消息.", "标题",JOptionPane.PLAIN_MESSAGE); // 只有一个确定按钮
	   // JOptionPane.showMessageDialog(null, "提示消息.", "标题",JOptionPane.ERROR_MESSAGE);
	   String inputValue = JOptionPane.showInputDialog("请输入你给我金额");
	   
	}	
	
    /** 
     * frame中的控件自适应frame大小：改变大小位置和字体 
     * @param frame 要控制的窗体 
     * @param proportion 当前和原始的比例 
     */  
    public static void modifyComponentSize(JFrame frame,double proportionW,double proportionH){  
        
        try   
        {  
            Component[] components = frame.getRootPane().getContentPane().getComponents();  
            for(Component co:components)  
            {   
            	double locX = co.getX() * proportionW;  
            	double locY = co.getY() * proportionH;  
            	double width = co.getWidth() * proportionW;  
            	double height = co.getHeight() * proportionH;  
                co.setLocation((int)locX, (int)locY);  
                co.setSize((int)width, (int)height);  
                int size = (int)(co.getFont().getSize() * proportionH);  
                Font font = new Font(co.getFont().getFontName(), co.getFont().getStyle(), size);  
                co.setFont(font);  
            }  
        }   
        catch (Exception e){}   
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
    
/*    BorderLayout边框布局
 * EAST 
          东区域的布局约束（容器右边）。
WEST 
          西区域的布局约束（容器左边）。
SOUTH 
          南区域的布局约束（容器底部）。
NORTH 
          北区域的布局约束（容器顶部）。
CENTER 
          中间区域的布局约束（容器中央）。
 * 
 * 
 */
}
