package com.bsu.truthtables.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;


@Component
public class Parser {

    @Value("${operators}")
    private String operators;   //loaded from applicaiton properties, this is all the known operators
    private int rows;

    public String parseChars(String question) {
        return question.replaceAll("[" + operators + "]", "").replaceAll(",", "").replaceAll(" ", "").chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
    }

    public Object parseQuestion(String question) throws Exception {
        //example string as of now = A | B, B ^ C | D. calling parse chars should return ABCD in order it was added. for example B | A | C would return BAC

       //todo work your magic here Kyle!
        ArrayList<ArrayList<String>> parsed = new ArrayList<>();

        String lits = parseChars(question);
        rows = (int)Math.pow(2, lits.length());

        ArrayList<String> statements = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();


        String[] tokens = question.split(",");
        for(String token: tokens) {
            statements.add(token);
            values.add(evaluateStatement(token));
        }

        parsed.add(statements);
        parsed.add(values);
        //thoughts, do we save the worked table in the database on parse each on each time pages is loaded? if we do store in db how should we save it?
        // TTTFTTFFTTFTFFTT could be a string and if we always go width by height first then fill so TTT, FTT, FFT, etc would be the top three boxes reading left to right and down. idk
        //def open to ideas

        return parsed;
    }

    public String evaluateStatement(String statement) throws Exception{
        String operator = "";
        for(int i = 0; i < statement.length(); i++) {
            String op = "";
            op += statement.charAt(i);
            if(operators.contains(op)) {
                operator = op;
                break;
            }
        }
        if(operator.isEmpty()) {
            throw new Exception("Error: No operator in statement '" + statement + "'");
        }
        String solution = "";
        String var1 = "TTFF";
        String var2 = "TFTF";
        for(int i = 0; i < rows; i++) {
            boolean v1 = var1.charAt(i) == 'T';
            boolean v2 = var2.charAt(i) == 'T';
            if(operator.equals("^")) {
                solution += v1 && v2 ? "T": "F";
            }
            else if(operator.equals("v")) {
                solution += v1 || v2 ? "T": "F";
            }
            else if(operator.equals(">")) {
                solution += (!v1 || v2) ? "T": "F";
            }
        }
        return solution;
    }

}
