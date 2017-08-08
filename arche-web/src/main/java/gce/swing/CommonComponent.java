package gce.swing;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CommonComponent extends JFrame {
    public CommonComponent() {

        // 面板.面板默认是流式布局 FlowLayout
        JPanel p1 = new JPanel();
        add(p1);

        // 标签
        JLabel name = new JLabel("用户名:");
        p1.add(name);

        // 文本域
        JTextField field = new JTextField(8);
        p1.add(field);

        // 标签
        JLabel passwd = new JLabel("密码");
        p1.add(passwd);
        // 密码域
        JPasswordField pass = new JPasswordField(8);
        p1.add(pass);

        // 单选按钮
        JLabel gender = new JLabel("性别");
        p1.add(gender);
        JRadioButton male = new JRadioButton("男");
        JRadioButton female = new JRadioButton("女");
        // 单选按钮组,同一个单选按钮组的互斥.
        ButtonGroup group = new ButtonGroup();
        group.add(male);
        group.add(female);
        // 注意,单选按钮组不能添加进容器
        p1.add(male);
        p1.add(female);

        // 复选框
        JLabel like = new JLabel("爱好:");
        p1.add(like);
        JCheckBox eat = new JCheckBox("吃饭");
        JCheckBox movie = new JCheckBox("看电影");
        JCheckBox sleep = new JCheckBox("睡觉");
        p1.add(eat);
        p1.add(movie);
        p1.add(sleep);

        // 文本域
        JLabel info = new JLabel("个人简介");
        p1.add(info);
        JTextArea area = new JTextArea(20, 20);
        p1.add(area);

        // 列表
        String[] data = { "one", "two", "three" };
        JList list = new JList(data);
        p1.add(list);

        // 普通按钮
        JButton button = new JButton("注册");
        p1.add(button);

        // 菜单条
        JMenuBar bar = new JMenuBar();
        // 菜单
        JMenu menu = new JMenu("文件");
        // 菜单选项
        JMenuItem myNew = new JMenuItem("新建");
        JMenuItem myOpen = new JMenuItem("打开");
        bar.add(menu);
        menu.add(myNew);
        menu.add(myOpen);
        add(bar, BorderLayout.NORTH);

    }

    public static void main(String[] args) {
        CommonComponent frame = new CommonComponent();
        frame.setTitle("常用组件");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 自适应
        frame.pack();
        frame.setVisible(true);
    }
}
