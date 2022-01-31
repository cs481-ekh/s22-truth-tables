package com.bsu.truthtables.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Parser {

    @Value("${operators}")
    private String operators;   //loaded from applicaiton properties, this is all the known operators

    public int parseChars(String question) {
        return (int) question.replaceAll("[" + operators + "]", "").replaceAll(",", "").replaceAll(" ", "").chars().distinct().count();
    }
    public Object parseQuestion(String question) {
        //example string as of now = A | B, B ^ C | D. calling parse chars should return ABCD in order it was added. for example B | A | C would return BAC

       //todo work your magic here Kyle!

        //thoughts, do we save the worked table in the database on parse each on each time pages is loaded? if we do store in db how should we save it?
        // TTTFTTFFTTFTFFTT could be a string and if we always go width by height first then fill so TTT, FTT, FFT, etc would be the top three boxes reading left to right and down. idk
        //def open to ideas

        return null;
    }

}
