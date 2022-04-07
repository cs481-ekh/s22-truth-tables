package com.bsu.truthtables.parser;

import com.bsu.truthtables.domain.ParsedQuestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import org.javatuples.Pair;


@Component
public class Parser {

    @Value("${operators}")
    private String operators;   //loaded from applicaiton properties, this is all the known operators
    private int rows;
    private String stmt;
    private String original;
    private String chars;
    @Value("#{${prefilledBox}}")
    private Map<String, String> prefilledBox;
    private HashMap<String, String> map = null;
    public ArrayList<ArrayList<String>> data;
    private ParsedQuestion parsedQuestion = null;


    public String orderResults(ArrayList<Pair<String, String>> r) {
        int max = 0;
        for(Pair<String,String> p : r){
            if(p.getValue1().length() > max) {
                max = p.getValue1().length();
            }
        }
        String res = "";
        for(int i = 0; i < max; i++) {
            for(Pair<String,String> p : r){
                if(p.getValue1().length() != 0) {
                    res += p.getValue1().charAt(i);
                }
            }
        }
        return res;
    }
    public String parseChars(String question) {
        return  question.replaceAll("[" + operators + "]", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll(" ", "")
                .replaceAll("\\:", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .chars()
                .distinct()
                .mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
    }

    public ParsedQuestion parseQuestion(String question) {
        parsedQuestion = new ParsedQuestion();
        map = new LinkedHashMap<>();
        this.stmt = question.replaceAll(" ", "");
        this.original=stmt;
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
        determineType();
        parsedQuestion.setResultList( getData());
        if(parsedQuestion.isConsistency()) {
            evalConsistency();
        }
        else if(parsedQuestion.isArgument()) {
            evalArgument();
        }
        else if(parsedQuestion.isEquivalence()) {
            evalEquivalence();
        }
        else {
            evalLogical();
        }
        parsedQuestion.setMap(map);
        return parsedQuestion;
    }

    public void evalArgument() {
        String[] list = original.split(":.");
        String[] premises = list[0].split(",");
        String conclusion = list[1];
        String validity = "";
        String valid = "valid";
        for(int i = 0; i < get(conclusion).length(); i++) {
            boolean allPremisesTrue = true;
            String tmp = "F";
            for(String premise: premises) {
                if(map.get(premise).charAt(i) == 'F') allPremisesTrue = false;
            }
            if(allPremisesTrue && map.get(conclusion).charAt(i) == 'F') {
                tmp = "T";
                valid = "invalid";
            }
            validity += tmp;
        }
        parsedQuestion.setFinalAnswer(valid);
        parsedQuestion.setShowsInvalid(validity);
    }

    public void evalConsistency() {
        String[] list = original.split(",");
        String consistent = "";
        String cons = "not consistent";
        for(int i = 0; i < map.get(list[0]).length(); i++) {
            String tmp = "T";
            for (String s : list) {
                if (map.get(s).charAt(i) == 'F') {
                    tmp = "F";
                }
            }
            if(tmp.equals("T")) cons = "consistent";
            consistent += tmp;
        }
        parsedQuestion.setShowsConsistent(consistent);
        parsedQuestion.setFinalAnswer(cons);
    }

    public void evalEquivalence() {
        String[] list = original.split("::");
        String equivalent = "";
        String equiv = "equivalent";
        for(int i = 0; i < map.get(list[0]).length(); i++) {
            String tmp = "T";
            char c1 = map.get(list[0]).charAt(i);
            char c2 = map.get(list[1]).charAt(i);
            if(c1 != c2) {
                equiv = "not equivalent";
                tmp = "F";
            }
            equivalent += tmp;
        }
        parsedQuestion.setShowsEquivalent(equivalent);
        parsedQuestion.setFinalAnswer(equiv);
    }

    public void evalLogical() {
        String values = map.get(original);
        String logical = "";
        boolean taut = true;
        boolean contra = true;
        for(int i = 0; i < values.length(); i++) {
            char c = values.charAt(i);
            String tmp = "T";
            if(c == 'T') {
                contra = false;
            }
            else {
                tmp = "F";
                taut = false;
            }
            logical += tmp;
        }
        parsedQuestion.setShowsNotTautology(logical);
        String status = "";
        if(taut) status = "tautology";
        else if(contra) status = "contradiction";
        else status = "contingent";
        parsedQuestion.setFinalAnswer(status);
    }

    public Object determineType() {
        if (stmt.contains("::")) {
            parsedQuestion.setEquivalence(true);
            stmt = stmt.replaceAll("::", ",");
            while(stmt.contains(",")) {
                stmt();
                map.put(",","");
            }
            return stmt();
        }
        else if(stmt.contains(":.")) {
            parsedQuestion.setArgument(true);
            stmt = stmt.replaceAll(":.", ",");
            while(stmt.contains(",")) {
                stmt();
                map.put(",","");
            }
            return stmt();
        }
        else if (stmt.contains(",")) {
            parsedQuestion.setConsistency(true);
            while(stmt.contains(",")) {
                stmt();
                map.put(",","");
            }
            return stmt();
        }
        parsedQuestion.setLogical(true);
        while(stmt.contains(",")) {
            stmt();
            map.put(",","");
        }
        return stmt();
    }

    public Object stmt() {
        Object obj = t0();
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
        while(peek() == '~') {
            eat('~');
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
            while(peek() == ')'){
                eat(')');
            }

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
        if(op == '-') {
            retval = (String) ifThen(name,o2);
            name = name + "->" + o2;
        }
        if(op == '<') {
            retval = (String) ifAndOnlyIf(name,o2);
            name = name + "<->" + o2;
        }
        put(name, get(retval));
        return name;

    }
    public Object not(Object o1) {
        String retVal = "";
        String name = "~" + o1;
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
        String name = "" + o1;
        put(name, get(o1));
        String retVal = "";
        for(int i = 0; i < get(name).length(); i++){
            retVal += get(name).charAt(i) == 'T' && get(o2).charAt(i) == 'F' ? "F" : "T";
        }
        name = name + "->" + o2;
        put(name, retVal);
        return name;
    }

    public Object ifAndOnlyIf(Object o1, Object o2) {
        String name = "" + o1;
        put(name, get(o1));
        String retVal = "";
        for(int i = 0; i < get(name).length(); i++){
            retVal += get(name).charAt(i) == get(o2).charAt(i) ? "T" : "F";
        }
        name = name + "<->" + o2;
        put(name, retVal);
        return name;
    }

    //helper methods for parser
    private char peek() {
        if(stmt.length() == 0) {
            return '0';
        }
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

    public ArrayList<Pair<String, String>> getData() {
        ArrayList<String> ops = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        int len = 1;
        String question = "";
        for(String key: map.keySet()) {
            if(key.length() > 1) {
                keys.add(key);
                if (key.length() > len) {
                    len = key.length();
                    question = key;
                }
            }
        }
        keys.sort(new KeyComparator(original));
        for(String key: keys) {
            String val = map.get(key);
            values.add(val);
        }
        for(int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);

            //clean up
            if(c == '^' || c == 'v' || c == '-' || c == '~' ) {
                String op = "" + c;
                ops.add(op);
            }
        }
        ArrayList<Pair<String, String>> ret = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < original.length(); i++) {
            String s = "" + original.charAt(i);
            if(count < ops.size() && s.equals(ops.get(count))) {

                if (original.charAt(i) == '~') {
                    ret.add(new Pair<>( new String(Character.toChars(0x00AC)), values.get(0)));
                } else if(original.charAt(i) == '^'){
                    ret.add(new Pair<>( new String(Character.toChars(0x2227)), values.get(0)));
                } else if(original.charAt(i) == '^') {
                    ret.add(new Pair<>( new String(Character.toChars(0x2228)), values.get(0)));
                }

                else {
                    ret.add(new Pair<>(s, values.get(count)));
                    count++;
                }
            } else if (original.charAt(i) == ':' && original.length() > i && original.charAt(i + 1) == ':') {
                ret.add(new Pair<>( new String(Character.toChars(8594)), values.get(0)));  //using address 0 because values currently doesnt contain a set for ::, this will use count once evaluation is completed
                i++; //skip over the next char since we already handle it

            } else if (original.charAt(i) == ':' && original.length() > i && original.charAt(i + 1) == '.') {
                ret.add(new Pair<>( new String(Character.toChars(8756)), values.get(0)));  //using address 0 because values currently doesnt contain a set for :., this will use count once evaluation is completed
                i++; //skip over the next char since we already handle it
            }

            else {
                ret.add(new Pair<>(s, ""));
            }
        }
        return ret;
    }

    public class KeyComparator implements Comparator<String> {
        public String question;
        public KeyComparator(String question) {
            this.question = question;
        }
        @Override
        public int compare(String o1, String o2) {
            int pos1 = findPos(o1, this.question);
            int pos2 = findPos(o2, this.question);
            if(pos1 < pos2) {
                return -1;
            }
            else if(pos1 > pos2) {
                return 1;
            }
            int len1 = o1.length();
            int len2 = o2.length();
            if(len1 < len2) {
                return -1;
            }
            return 1;
        }

        public int findPos(String s, String question) {
            int pos = 0;
            boolean found = false;
            for(; pos < question.length(); pos++) {
                for(int i = 0; i < s.length(); i++) {
                    if(!(s.charAt(i) == question.charAt(pos+i))) {
                        break;
                    }
                    if(i == s.length()-1) {
                        found = true;
                    }
                }
                if(found) break;
            }
            int parenCheck = 0;
            while(s.charAt(parenCheck) == '(') {
                parenCheck++;
                pos++;
            }
            return pos;
        }
    }
}
