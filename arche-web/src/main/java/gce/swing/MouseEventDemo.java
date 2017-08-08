package gce.swing;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * 事件和事件源
 * 在运行java图形用户界面程序时,用户与程序交互,用户执行了某些操作时，会发生一些事情， 
 * 那么事件(event)可以定义为程序发生了某些事情的信号.
 * 典型用户操作就是用户移动鼠标,点击按钮,敲击键盘等.程序可以选择相应事件或者忽略事件。

    能够创建一个事件并触发该事件的组件称为源对象。例如由于按钮能够点击, 
    那么按钮就是一个源对象,按钮被点击就是一个事件。

一个事件是事件类的实例对象。事件类的根类是java.util.EventObject。 

事件对象包含事件相关的属性，可以使用EventObject类中的实例方法getSource获得事件的源对象。
 * 
 * 监听器

当源对象触发了一个事件,谁来处理这个事件？在Java中对此感兴趣的对象会处理它。对此感兴趣的对象称之为监听器（Listener）。

举例来说当我们点击一个按钮，想要按钮执行一些动作，需要一个对象来监控按钮，当点击按钮的事件发生时，该对象就会监听到按钮事件。进而可以执行一些动作。
 * 
 * 
 *
 */
public class MouseEventDemo extends JFrame {
    MouseEventDemo() {
        JButton button = new JButton("ok");
        JTextArea text = new JTextArea();
        add(button, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);

        button.addMouseListener(new MouseListener() {

            // 鼠标按钮在组件上释放时调用。
            public void mouseReleased(MouseEvent e) {
                System.out.println("鼠标释放");

            }

            // 鼠标按键在组件上按下时调用。
            public void mousePressed(MouseEvent e) {
                System.out.println("鼠标按下组件");

            }

            // 鼠标离开组件时调用。
            public void mouseExited(MouseEvent e) {
                System.out.println("鼠标离开组件");

            }

            // 鼠标进入到组件上时调用。
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入
                System.out.println("鼠标进入组件");

            }

            // 鼠标按键在组件上单击（按下并释放）时调用。
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标单击组件");

            }
        });
        text.addKeyListener(new KeyListener() {

            // 键入某个键时调用此方法。
            public void keyTyped(KeyEvent e) {
                System.out.println("键入某个键");
                if (e.getKeyChar() == 'q') {
                    System.exit(0);
                }
            }
            // 释放某个键时调用此方法。
            public void keyReleased(KeyEvent e) {
                System.out.println("按键释放");

            }

            // 按下某个键时调用此方法。
            public void keyPressed(KeyEvent e) {
                System.out.println("键盘按下");
            }
        });
    }

    public static void main(String[] args) {
        MouseEventDemo frame = new MouseEventDemo();
        frame.setTitle("鼠标事件");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
