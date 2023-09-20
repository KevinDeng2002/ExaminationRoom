import util.MessageWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;



public class CountDownPanel extends JPanel {

    private Timer timer;
    private long startTime = -1;
    private long duration = 1000*15*60;

    private JLabel label;

    public CountDownPanel() {
        this.setVisible(true);
        setLayout(new GridBagLayout());
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }
                long now = System.currentTimeMillis();
                long clockTime = now - startTime;
                if (clockTime >= duration) {
//                    clockTime = duration;
//                    GUI.confirm = new MessageWindow("确定交卷吗？");
//                    GUI.confirm.addActionListener(actionEvent -> {
//                        GUI.mainWindow.vanish();
//                        // 主面板消失
//                        // 开始算分
//                        int score = 0;
//                        for (int i = 0; i < 25; i++) {
//                            System.out.println("question "+i+":");
//                            System.out.println("your answer:" + GUI.answers.get(i));
//                            System.out.println("expected answer:" + GUI.questions.get(i).getAnswer());
//                            if (GUI.answers.get(i).equals(GUI.questions.get(i).getAnswer())) {
//                                score += 4;
//                            }
//                        }
//                        GUI.confirm.vanish();
//                        new MessageWindow("您的得分：" + score, e1 -> {
//                            System.gc();
//                            GUI.mainWindow.show();
//                            GUI.questions = Launcher.questionBank.getRandomQuestions(25);
//                            GUI.questionPanels.forEach(jPanel -> jPanel.setVisible(false));
//                            GUI.questionPanels.clear();
//                            GUI.exam();
//                        });
//                    });
                    timer.stop();
                }
                SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                long t = duration - clockTime;
                label.setText(df.format(duration - clockTime));
            }
        });
        timer.setInitialDelay(0);
        timer.start();
        label = new JLabel("考试时间：");
        add(label);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 100);
    }
}