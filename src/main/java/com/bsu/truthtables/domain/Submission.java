package com.bsu.truthtables.domain;


public class Submission {

    public String parsedAnswers;
    public int count = 0; //used for TF
    public int count2 = 0; //used for validity

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
    public String getCount2() {
        return String.valueOf(count2++);
    }
}
