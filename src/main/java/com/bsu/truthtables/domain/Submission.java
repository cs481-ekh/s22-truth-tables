package com.bsu.truthtables.domain;

import org.javatuples.Pair;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;

public class Submission {
    public ArrayList<String> answers;
    public String answer;
    public String parsedAnswers;

    public Submission(String parsedAnswers) {
        this.answers = new ArrayList<>();
        this.parsedAnswers = parsedAnswers;
    }

    public String getAnswer() {
        return "";
    }

    public void setAnswer(String answer) {
        answers.add(answer);
    }

    public String getParsedAnswers() {
        return parsedAnswers;
    }

    public void setParsedAnswers(String parsedAnswers) {
        this.parsedAnswers = parsedAnswers;
    }

}
