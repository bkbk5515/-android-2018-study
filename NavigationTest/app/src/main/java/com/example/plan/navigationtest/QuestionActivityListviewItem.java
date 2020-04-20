package com.example.plan.navigationtest;

public class QuestionActivityListviewItem {

    private String title, answer ;

    public QuestionActivityListviewItem(String title, String answer){
        this.title = title;
        this.answer = answer;
    }

    public QuestionActivityListviewItem(){    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
