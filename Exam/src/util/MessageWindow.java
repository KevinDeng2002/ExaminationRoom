package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MessageWindow extends Window{
    private final JButton button;
    /**
     * 消息窗口类，继承窗口实现简单的提示信息
     * @param msg 提示内容
     */
    public MessageWindow(String msg) {
        super("提示", new Dimension(300,150));
        super.setLocation(400,400);
        super.getJFrame().setLayout(new FlowLayout(FlowLayout.CENTER,50,20));
        JLabel message = new JLabel(msg);
        button = new JButton("确定");
        button.addActionListener(actionEvent -> super.vanish());
        super.getJFrame().add(message);
        super.getJFrame().add(button);
        super.show();
    }

    /**
     * 支持自定义点击”确定“后的动作
     * @param msg 提示内容
     * @param e 动作
     */
    public MessageWindow(String msg,ActionListener e){
        super("提示", new Dimension(300,150));
        super.setLocation(400,400);
        super.getJFrame().setLayout(new FlowLayout(FlowLayout.CENTER,50,20));
        JLabel message = new JLabel(msg);
        button = new JButton("确定");
        button.addActionListener(e);
        super.getJFrame().add(message);
        super.getJFrame().add(button);
        super.show();
    }

    public void addActionListener(ActionListener e){
        button.addActionListener(e);
    }
}