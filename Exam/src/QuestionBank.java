import util.Question;
import util.QuestionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// 从txt中读取问题
// 生成题库
// 每个对象存储在链表中
public class QuestionBank extends LinkedList<Question>{
    public void load(File path){
        if(!path.exists()){
            throw new RuntimeException("未找到题库");
        }
        if(!path.getPath().endsWith(".txt")){
            throw new RuntimeException("文件格式错误");
        }

        ArrayList<String> content = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            // 按行读取txt试题文件
            for(Object obj:bufferedReader.lines().toArray()){
                content.add((String) obj);
            }
            for(int i = 0;i<content.size();){
                String line = content.get(i);
                // 读取类别
                QuestionType type = QuestionType.intToType(Integer.parseInt(String.valueOf(line.charAt(line.length()-2))));
                // 确定题号
                if(Character.isDigit(line.charAt(0))){
                    // 此时才创建question类型
                    Question question = new Question(type);
                    // 标号不读进类中
                    for(int j=0;j<line.length();j++){
                        if(line.charAt(j)=='.'){
                            line = line.substring(j+1);
                            break;
                        }
                    }
                    question.setContent(line);
                    ArrayList<String> choices = new ArrayList<>();
                    if(type.equals(QuestionType.SELECTION)){
                        choices.add(content.get(i+1));
                        choices.add(content.get(i+2));
                        choices.add(content.get(i+3));
                        choices.add(content.get(i+4));
                        question.setChoices(choices);
                        question.setAnswer(content.get(i+5));
                        i+=6;
                    }else if(type.equals(QuestionType.JUDGEMENT)){
                        choices.add(content.get(i+1));
                        choices.add(content.get(i+2));
                        question.setChoices(choices);
                        question.setAnswer(content.get(i+3));
                        i+=4;
                    }else{
                        question.setAnswer(content.get(i+1));
                        i+=2;
                    }
                    this.add(question);
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * 得到一个新的题库
     * @param number 生成题目的数量
     * @return 生成的题库
     */
    public List<Question> getRandomQuestions(int number){
        List<Question> questions;
        Collections.shuffle(this);
        questions = this.subList(0,number);
        return questions;
    }
}
