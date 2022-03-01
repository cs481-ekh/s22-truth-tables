package com.bsu.truthtables.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class Parser {

    @Value("${operators}")
    private String operators;   //loaded from applicaiton properties, this is all the known operators
    private int rows;
    private String stmt;
    private String chars;
    @Value("#{${prefilledBox}}")
    private Map<String, String> prefilledBox;
    private HashMap<String, String> map = null;


    public String parseChars(String question) {
        return  question.replaceAll("[" + operators + "]", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll(" ", "")
                .replaceAll(",", "")
                .chars()
                .distinct()
                .mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
    }

    public HashMap parseQuestion(String question) {
        map = new HashMap<>();
        this.stmt = question.replaceAll(" ", "");
        this.chars = parseChars(question);
        String values = prefilledBox.get(String.valueOf(chars.length()));
        int i = 0;
        while (i < values.length()) {
            if (get(chars.charAt(i % chars.length())) == null) {
                put(chars.charAt(i % chars.length()), String.valueOf(values.charAt(i)));  //this should map each literal to

            } else {
                put(chars.charAt(i % chars.length()), get(chars.charAt(i % chars.length())) + values.charAt(i));   //this should map each literal to
            }
            i++;
        }
        stmt();
        return map;
    }

    public Object stmt() {
        Object obj = t0();
//        while (more() && peek() == ':' && doublePeek() == '.') {
//            eat(':');
//            eat('.');
//            Object obj2 = stmt();
//            obj = therefore(obj, obj2);  //handle therefore
//        }
        return obj;
    }

    public Object t0() {
        Object obj = t1();
        if (more() && peek() == '<' && doublePeek() == '-' && triplePeek() == '>') {
            eat('<');
            eat('-');
            eat('>');
            Object obj2 = t1();
            return ifAndOnlyIf(obj, obj2);
        }
        return obj;
    }

    public Object t1() {
        Object obj = t2();
        if (more() && peek() == '-' && doublePeek() == '>') {
            eat('-');
            eat('>');
            Object obj2 = t2();
            return ifThen(obj, obj2);
        }
        return obj;
    }

    public Object t2() {
        Object obj = t3();
        while (more() && peek() == 'v') {
            eat('v');
            Object obj2 = t3();
            obj = or(obj, obj2);
        }
        return obj;
    }

    public Object t3() {
        Object obj = t4();
        while (more() && peek() == '^') {
            eat('^');
            Object obj2 = t4();
            obj = and(obj, obj2);
        }
        return obj;
    }

    public Object t4() {
        Object obj = t5();
        return obj;
    }

    public Object t5() {
        int numOfNots = 0;
        while(peek() == '!') {
            eat('!');
            numOfNots++;
        }
        if (numOfNots != 0 && numOfNots %2 == 1) {          //idk what the char is for not so I am using ! temporarily
            Object obj = t6();
            return not(obj);
        }
        return t6();
    }

    public Object t6() {
        if (more() && peek() == '(') {
            eat('(');
            Object s = stmt();
            eat(')');
             return paren(s);
        }
        return t7();
    }

    public Object t7() {
        return next();
    }


    //evaluate
    public Object paren(Object o1){
        String name = "(" + o1 + ")";
        put(name, get(o1));
        map.remove(o1);
        if(stmt.length() == 0) {
            return name;
        }
        char op = next();
        Object o2 = stmt();
        String retval = "";
        if(op == '^') {
            retval = (String) and(name,o2);
            name = name + "^" + o2;
        }
        if(op == 'v') {
            retval = (String) or(name,o2);
            name = name + "v" + o2;
        }
        put(name, get(retval));
        return name;

    }
    public Object not(Object o1) {
        String retVal = "";
        String name = "!" + o1;
        for (int i = 0 ; i < get(o1).length(); i++) {
            if(get(o1).charAt(i) == 'T') {
                retVal += "F";
            } else{
                retVal += "T";
            }

        }
        map.put(name, retVal);
        return name;
    }

    public Object and(Object o1, Object o2) {

        String retVal = "";
        String name = o1 + "^" + o2;
        for (int i = 0 ; i < get(o1).length(); i++) {
            if (get(o1).charAt(i) == 'T' && get(o2).charAt(i) == 'T') {
                retVal += "T";
            } else {
                retVal += "F";
            }
        }
        map.put(name, retVal);
        return name;
    }

    public Object or(Object o1, Object o2) {
        String retVal = "";
        String name = o1 + "v" + o2;
        for (int i = 0 ; i < get(o1).length(); i++) {
            if (get(o1).charAt(i) == 'T' || get(o2).charAt(i) == 'T') {
                retVal += "T";
            } else {
                retVal += "F";
            }
        }
        map.put(name, retVal);
        return name;
    }

    public Object ifThen(Object o1, Object o2) {
        String name = "(" + o1 + ")";
        put(name, get(o1));
        map.remove(o1);
        String retVal = "";
        for(int i = 0; i < get(name).length(); i++){
            retVal += get(name).charAt(i) == 'T' ? "T" : "F";
        }
        put(name + "->" + o2, retVal);
        return name;
    }

    public Object ifAndOnlyIf(Object o1, Object o2) {
        String name = "(" + o1 + ")";
        put(name, get(o1));
        map.remove(o1);
        String retVal = "";
        for(int i = 0; i < get(name).length(); i++){
            retVal += get(name).charAt(i) == 'T' && get(o2).charAt(i) == 'T' ? "T" : "F";
        }
        put(name + "<->" + o2, retVal);
        return name;
    }

    //helper methods for parser
    private char peek() {
        return stmt.charAt(0);
    }

    private char doublePeek() {
        if (stmt.length() < 1) {
            return '0';             //return 0 since it wont ever equal a literal, else it might throw error
        }
        return stmt.charAt(1);
    }

    private char triplePeek() {
        if (stmt.length() < 2) {
            return '0';                 //return 0 since it wont ever equal a literal, else it might throw error
        }
        return stmt.charAt(2);
    }

    private void eat(char c) {
        if (peek() == c)
            this.stmt = this.stmt.substring(1);
        else
            throw new RuntimeException("Expected: " + c + "; got: " + peek());
    }

    private void eatWhiteSpace() {
        if (stmt.length() > 0 && stmt.charAt(0) == ' ')
            this.stmt = this.stmt.substring(1);
    }

    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    private boolean more() {
        return stmt.length() > 0;
    }
    public String get(Object o){
        if(o instanceof String) {
            return map.get(o);
        } else {
            return map.get(String.valueOf(o));
        }
    }
    public String put(Object o, String val){
        if(o instanceof String) {

            return map.put(o.toString(), val);
        } else {
            return map.put(String.valueOf(o), val);
        }
    }
}
