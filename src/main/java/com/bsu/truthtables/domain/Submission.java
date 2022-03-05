package com.bsu.truthtables.domain;

import java.util.ArrayList;

public class Submission {
    public ArrayList<String> answers;
    public String answer;

    public Submission() {
        answers = new ArrayList<>();
    }

    public String getAnswer() {
        return "";
    }

    public void setAnswer(String answer) {
        answers.add(answer);
    }
}
