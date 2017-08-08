package gce.swing;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class SwingTest extends JFrame {
	   public static void main(String args[]) {
	       new SwingTest();
	   }

	   public SwingTest() {
	       super("SwingTextField 测试");
	       init();
	       setup();
	       this.setSize(800, 300);
	       this.setVisible(true);
	       this.setLocation
	               ( //定位框架位置
	                       (int) (Toolkit.getDefaultToolkit().getScreenSize().
	                              getWidth() - 400) / 2,
	                       (int) (Toolkit.getDefaultToolkit().getScreenSize().
	                              getHeight() - 300) / 2
	               );
	       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   }

	   BasePanel leftPanel;
	   BasePanel rightPanel;
	   JTextField country = new JTextField(16);
	   JButton search = new JButton("SEARCH");
	   JTextField latitude = new JTextField(40);
	   JTextField currentTime = new JTextField(40);
	   JTextField wind = new JTextField(40);
	   JTextField visibilityField = new JTextField(40);
	   JTextField skycondition = new JTextField(40);
	   JTextField dewpoint = new JTextField(40);
	   JTextField relativehumidity = new JTextField(40);
	   JTextField presure = new JTextField(40);

	   public void init() {
	       leftPanel = new BasePanel() {
	           public void initAllComponents() {
	           }

	           public void layoutAllComponents() {
	               addComponent(country, 0, 0, 1, 1, 10, 10);
	               addComponent(search, 1, 0, 1, 1, 10, 10);
	           }
	       };

	       rightPanel = new BasePanel() {
	           public void initAllComponents() {
	           }

	           public void layoutAllComponents() {
	               addComponent(latitude, 0, 0, 1, 1, 10, 10);
	               addComponent(currentTime, 1, 0, 1, 1, 10, 10);
	               addComponent(wind, 2, 0, 1, 1, 10, 10);
	               addComponent(visibilityField, 3, 0, 1, 1, 10, 10);
	               addComponent(skycondition, 4, 0, 1, 1, 10, 10);
	               addComponent(dewpoint, 5, 0, 1, 1, 10, 10);
	               addComponent(relativehumidity, 6, 0, 1, 1, 10, 10);
	               addComponent(presure, 7, 0, 1, 1, 10, 10);
	           }
	       };
	   }

	   public void setup() {
	       this.setLayout(new BorderLayout());
	       this.add(leftPanel, BorderLayout.WEST);
	       this.add(rightPanel, BorderLayout.EAST);
	   }
	}
