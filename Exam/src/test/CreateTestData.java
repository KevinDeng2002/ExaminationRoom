package test;

import util.Question;
import util.QuestionType;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

//生成500道测试题目
public class CreateTestData {
    public static void main(String[] args) {
        File file = new File("test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            String[] answers = {"choice A", "choice B", "choice C", "choice D"};

            // 开始生成
            for (int i = 1; i <= 500; i++)
            {
                // 3种题型
                Question temp = new Question(QuestionType.intToType(Math.abs(new Random().nextInt()) % 3 + 1));

                // 填空
                if (temp.getType() == QuestionType.FILLING) {
                    temp.setQandA("question" + i, "this is a filling answer");
                } else {
                    temp.setQandA("question" + i, answers[(Math.abs(new Random().nextInt())) % 4]);
                }
                ArrayList<String> choices = new ArrayList<>();
                // 选择
                if (temp.getType() == QuestionType.SELECTION) {
                    choices.add("choice A");
                    choices.add("choice B");
                    choices.add("choice C");
                    choices.add("choice D");
                }
                // 判断
                else if (temp.getType() == QuestionType.JUDGEMENT) {
                    choices.add("true");
                    choices.add("false");
                }
                temp.setChoices(choices);
                StringBuilder strToWrite = new StringBuilder();

                // 开始写入
                // 先声明题号与类型
                strToWrite.append(i).append(".").append(temp.getContent()).append("(").append(temp.getType()).append(")").append("\n");
                // 写入选项
                if (!temp.getChoices().isEmpty())
                    for (String choice : temp.getChoices()) {
                        strToWrite.append(choice).append("\n");
                    }
                // 写入答案
                strToWrite.append(temp.getAnswer()).append("\n");
                fileWriter.write(strToWrite.toString());
            }
            // 刷新缓存，开始下一次循环
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功创建测试题库，路径："+file.getAbsolutePath());
    }
}
