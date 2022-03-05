package com.bsu.truthtables.domain;


public class Submission {

    public String parsedAnswers;
    public int count = 0;

    public Submission(String parsedAnswers) {
        this.parsedAnswers = parsedAnswers;
    }

    public String getParsedAnswers() {
        return parsedAnswers;
    }

    public void setParsedAnswers(String parsedAnswers) {
        this.parsedAnswers = parsedAnswers;
    }

    public String getCount() {
        return String.valueOf(count++);
    }
}
