import util.MessageWindow;
import util.Question;
import util.QuestionType;
import util.Window;

import javax.security.auth.callback.TextInputCallback;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.JapaneseChronology;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


//用户界面
public class GUI {
     static Window mainWindow;

    // 25道题
     static List<Question> questions = Launcher.questionBank.getRandomQuestions(25);

     static final ArrayList<String> answers = new ArrayList<>();

    static {
        initWindow();
        for (int i = 0; i < 25; i++) {
            answers.add("");
        }
    }



    public static void show() {
        menu();
        mainWindow.show();
    }

    public static void initWindow(){
        mainWindow = new Window("考试系统", new Dimension(400, 600));
        mainWindow.getJFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 位置
        mainWindow.setLocation(300, 200);
        mainWindow.setBackground(Color.GRAY);
    }

    //主菜单布局
    public static void menu() {
        //字体设置
        Font titleFont = new Font("黑体", Font.BOLD, 24);

        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 100));

        JLabel label = new JLabel("Java高级语言程序设计考试");
        label.setFont(titleFont);
        menu.add(label);

        // 另一个面板放登录信息
        JPanel login = new JPanel();
        login.setLayout(new GridLayout(2, 2));
        JLabel number = new JLabel("学号：");
        JTextField numberTextField = new JTextField(20);
        JLabel password = new JLabel("密码：");
        JPasswordField passwordField = new JPasswordField(20);
        login.add(number);
        login.add(numberTextField);
        login.add(password);
        login.add(passwordField);
        menu.add(login);

        JButton button = new JButton("开始考试");
        button.addActionListener(actionEvent -> {
            if (Launcher.accountManager.check(numberTextField.getText(), passwordField.getText())) {
                // 密码正确进去考试界面
                menu.setVisible(false);
                exam();

            } else {
                new MessageWindow("用户名或密码不正确！");
            }
        });
        menu.add(button);
        mainWindow.addComp(menu);
    }

    static class MainThread extends Thread
    {
        @Override
        public void run()
        {
            exam();
        }
    }

    public static ArrayList<JPanel> questionPanels = new ArrayList<>();
    public static JPanel exam;

    //考试界面
    // 记录这是第几次考试
    static int sequence = 0;
    public static void exam() {
        // 重置面板--显示考试面板
        mainWindow.vanish();
        initWindow();
        mainWindow.show();

        exam = new JPanel();

        JPanel title = new JPanel();
        title.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel question = new JLabel("题目：");
        title.add(question);
        exam.add(title);

        // 计时器面板
        CountDownPanel counter = new CountDownPanel();
        counter.setLayout(new FlowLayout(FlowLayout.RIGHT));
        exam.add(counter);


        for (int i = 0; i < 25; i++) {
            questionPanels.add(nextQuestion(i));
        }

        // 初始化
        AtomicInteger number = new AtomicInteger(0);//记录题号
        AtomicReference<JPanel> nowPanel = new AtomicReference<>(questionPanels.get(0));


        JPanel p1 = new JPanel();
        JButton prev = new JButton("上一题");
        JButton next = new JButton("下一题");
        prev.addActionListener(actionEvent -> {
            if (number.get() > 0) {
                nowPanel.get().setVisible(false);
                nowPanel.set(questionPanels.get(number.get() - 1));
                p1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
                p1.add(prev);
                p1.add(next);
                exam.add(nowPanel.get());
                exam.add(p1);
                number.getAndDecrement();
                nowPanel.get().setVisible(true);
            }
        });
        next.addActionListener(actionEvent -> {
            if (number.get() < 24) {
                nowPanel.get().setVisible(false);
                nowPanel.set(questionPanels.get(number.get() + 1));
                p1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
                p1.add(prev);
                p1.add(next);
                exam.add(nowPanel.get());
                exam.add(p1);
                number.getAndIncrement();
                nowPanel.get().setVisible(true);
            }
        });
        p1.add(prev);
        p1.add(next);
        exam.add(nowPanel.get());
        exam.add(p1);
        mainWindow.addComp(exam);
        exam.setVisible(true);
        // 计时提示
        new MessageWindow("考试时间为：15分钟。计时开始!");
    }

//生成每一道题的答题面板
    public static JPanel nextQuestion(int number) {
        JPanel res = new JPanel();
        res.setLayout(new GridLayout(4, 1));
        TextArea questionTextArea = new TextArea(4, 30);
        res.add(questionTextArea);

        Question question = questions.get(number);

        questionTextArea.setText("第 " + (number + 1) + " 题：" + question.getContent());
        questionTextArea.setEditable(false);

        JLabel answerLabel = new JLabel("您的回答：");
        res.add(answerLabel);

        JRadioButton choiceA;
        JRadioButton choiceB;
        JRadioButton choiceC;
        JRadioButton choiceD;
        TextField answerTextField;

        // 填空题
        if (question.getType().equals(QuestionType.FILLING)) {
            answerTextField = new TextField(5);
            answerTextField.addTextListener(e -> answers.set(number,answerTextField.getText()));
            res.add(answerTextField);
        }

        // 选择题
        else if (question.getType().equals(QuestionType.SELECTION)) {
            JPanel choices = new JPanel();
            choices.setLayout(new GridLayout(2, 2, 150, 20));
            choiceA = new JRadioButton(question.getChoices().get(0));
            choiceB = new JRadioButton(question.getChoices().get(1));
            choiceC = new JRadioButton(question.getChoices().get(2));
            choiceD = new JRadioButton(question.getChoices().get(3));
            choiceA.addActionListener(actionEvent -> {
                // set() 方法用于替换动态数组中指定索引的元素
                answers.set(number, choiceA.getText());
                choiceB.setSelected(false);
                choiceC.setSelected(false);
                choiceD.setSelected(false);
            });
            choiceB.addActionListener(actionEvent -> {
                answers.set(number, choiceB.getText());
                choiceA.setSelected(false);
                choiceC.setSelected(false);
                choiceD.setSelected(false);
            });
            choiceC.addActionListener(actionEvent -> {
                answers.set(number, choiceC.getText());
                choiceA.setSelected(false);
                choiceB.setSelected(false);
                choiceD.setSelected(false);
            });
            choiceD.addActionListener(actionEvent -> {
                answers.set(number, choiceD.getText());
                choiceA.setSelected(false);
                choiceB.setSelected(false);
                choiceC.setSelected(false);
            });
            choices.add(choiceA);
            choices.add(choiceB);
            choices.add(choiceC);
            choices.add(choiceD);
            res.add(choices);
        }

        // 判断题
        else if (question.getType().equals(QuestionType.JUDGEMENT)) {
            JPanel choices = new JPanel();
            choices.setLayout(new GridLayout(2, 2, 150, 20));
            choiceA = new JRadioButton(question.getChoices().get(0));
            choiceB = new JRadioButton(question.getChoices().get(1));
            choiceA.addActionListener(actionEvent -> {
                answers.set(number, "choice A");
                choiceB.setSelected(false);
            });
            choiceB.addActionListener(actionEvent -> {
                answers.set(number, "choice B");
                choiceA.setSelected(false);
            });
            choices.add(choiceA);
            choices.add(choiceB);
            res.add(choices);
        }

        // 最后一题可以交卷
        if (number == 24) {
            JPanel endPanel = new JPanel();
            endPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JButton end = new JButton("交卷");
            endPanel.add(end);
            res.add(endPanel);
                    end.addActionListener(actionEvent -> {
                confirm = new MessageWindow("确定交卷吗？");
                confirm.addActionListener(e -> {
                    mainWindow.vanish();
                    // 主面板消失
                    // 开始算分
                    // 将本次的题目和标准答案输入到文件中
                    sequence ++;
                    File file = new File("answersheet" + sequence +".txt");
                    //if file doesnt exists, then create it
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    FileWriter fileWritter = null;
                    try {
                        fileWritter = new FileWriter(file.getName(),true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    for (int i = 0; i < 25; i++) {
                        try {
                            fileWritter.write("question "+i+":"+"\n");
                            fileWritter.write("answer:" + questions.get(i).getAnswer()+"\n");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
//                        System.out.println();
//                        System.out.println("your answer:" + answers.get(i));
//                        System.out.println("expected answer:" + questions.get(i).getAnswer());
                    }
                    try {
                        fileWritter.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    int score = 0;
                    for (int i = 0; i < 25; i++) {
                        System.out.println("question "+i+":");
                        System.out.println("your answer:" + answers.get(i));
                        System.out.println("expected answer:" + questions.get(i).getAnswer());
                        if (answers.get(i).equals(questions.get(i).getAnswer())) {
                            score += 4;
                        }
                    }
                    confirm.vanish();
                    new MessageWindow("您的得分：" + score, e1 -> {
                        System.gc();
//                        System.exit(0);
                        mainWindow.show();
                        int n = JOptionPane.showConfirmDialog(null, "是否再试一次？", "提示",JOptionPane.YES_NO_OPTION);//返回的是按钮的index  i=0或者1
                        if(n==1){
                            sequence = 0;
                            System.exit(0);
                        }
                        if(n==0)
                        {
                            questions = Launcher.questionBank.getRandomQuestions(25);
                            questionPanels.forEach(jPanel -> jPanel.setVisible(false));
                            questionPanels.clear();
                            exam();
                        }
                    });
                });
            });
        }
        return res;
    }
    public static MessageWindow confirm;
}