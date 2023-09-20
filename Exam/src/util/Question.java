package util;

import java.util.ArrayList;

// 问题对象
// 属性包括：题型、内容、答案、选项

public class Question {

    private String content;
    private final QuestionType type;
    private String answer;
    private ArrayList<String> choices;

    /**
     * 题目类
     * @param type 题目类型
     */
    public Question(QuestionType type){
        this.type = type;
    }
    public void setQandA(String content,String answer){
        this.content = content;
        this.answer = answer;
    }
    public void setContent(String content){
        this.content = content;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public QuestionType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getAnswer() {
        return answer;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }
}