package com.bsu.truthtables.domain;

import lombok.Data;

@Data
public class Question {

    private int id;
    private int chars;
    private String question;
    private String chapter;

}
