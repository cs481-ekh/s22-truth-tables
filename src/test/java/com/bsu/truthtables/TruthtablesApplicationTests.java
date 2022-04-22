package com.bsu.truthtables;

import com.bsu.truthtables.domain.ParsedQuestion;
import com.bsu.truthtables.parser.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TruthtablesApplicationTests {

    @Autowired
    Parser parser;

    @Test
    void contextLoads() {
    }

    @Test
    void parserTests() {
        //Basic operator tests
        ParsedQuestion question = parser.parseQuestion("A v B");
        assert(question.getResultList().get(1).getValue0().equals("v"));
        assert(question.getResultList().get(1).getValue1().equals("TTTF"));

        question = parser.parseQuestion("A ^ B");
        assert(question.getResultList().get(1).getValue0().equals("^"));
        assert(question.getResultList().get(1).getValue1().equals("TFFF"));

        question = parser.parseQuestion("A -> B");
        assert(question.getResultList().get(1).getValue0().equals(new String(Character.toChars(8594))));
        assert(question.getResultList().get(1).getValue1().equals("TFTT"));

        question = parser.parseQuestion("A <-> B");
        assert(question.getResultList().get(1).getValue0().equals(new String(Character.toChars(8596))));
        assert(question.getResultList().get(1).getValue1().equals("TFFT"));

        question = parser.parseQuestion("~A");
        assert(question.getResultList().get(0).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(0).getValue1().equals("FT"));

        //Parentheses test
        question = parser.parseQuestion("A^(BvC)");
        assert(question.getResultList().get(1).getValue0().equals("^"));
        assert(question.getResultList().get(1).getValue1().equals("TTTFFFFF"));
        assert(question.getResultList().get(4).getValue0().equals("v"));
        assert(question.getResultList().get(4).getValue1().equals("TTTFTTTF"));

        //Equivalence test
        question = parser.parseQuestion("AvB::BvA");
        assert(question.getResultList().get(1).getValue0().equals("v"));
        assert(question.getResultList().get(1).getValue1().equals("TTTF"));
        assert(question.getResultList().get(5).getValue0().equals("v"));
        assert(question.getResultList().get(5).getValue1().equals("TTTF"));
        assert(question.getResults().equals("FFFF"));
        assert(question.getFinalAnswer().equals("equivalent"));

        //Non-equivalence test
        question = parser.parseQuestion("AvB::A^B");
        assert(question.getResultList().get(1).getValue0().equals("v"));
        assert(question.getResultList().get(1).getValue1().equals("TTTF"));
        assert(question.getResultList().get(5).getValue0().equals("^"));
        assert(question.getResultList().get(5).getValue1().equals("TFFF"));
        assert(question.getResults().equals("FTTF"));
        assert(question.getFinalAnswer().equals("not equivalent"));

        //Valid argument test
        question = parser.parseQuestion("A, B :. A^B");
        assert(question.getResultList().get(5).getValue0().equals("^"));
        assert(question.getResultList().get(5).getValue1().equals("TFFF"));
        assert(question.getResults().equals("FFFF"));
        assert(question.getFinalAnswer().equals("valid"));

        //Invalid argument test
        question = parser.parseQuestion("A, ~B :. A^B");
        assert(question.getResultList().get(2).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(2).getValue1().equals("FTFT"));
        assert(question.getResultList().get(6).getValue0().equals("^"));
        assert(question.getResultList().get(6).getValue1().equals("TFFF"));
        assert(question.getResults().equals("FTFF"));
        assert(question.getFinalAnswer().equals("invalid"));

        //Consistency test
        question = parser.parseQuestion("A v B, ~A");
        assert(question.getResultList().get(1).getValue0().equals("v"));
        assert(question.getResultList().get(1).getValue1().equals("TTTF"));
        assert(question.getResultList().get(4).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(4).getValue1().equals("FFTT"));
        assert(question.getResults().equals("FFTF"));
        assert(question.getFinalAnswer().equals("consistent"));

        //Inconsistency test
        question = parser.parseQuestion("A, ~A");
        assert(question.getResultList().get(2).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(2).getValue1().equals("FT"));
        assert(question.getResults().equals("FF"));
        assert(question.getFinalAnswer().equals("not consistent"));

        //Tautology test
        question = parser.parseQuestion("Av(~A)");
        assert(question.getResultList().get(3).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(3).getValue1().equals("FT"));
        assert(question.getResults().equals("FF"));
        assert(question.getFinalAnswer().equals("tautology"));

        //Contradiction test
        question = parser.parseQuestion("A^(~A)");
        assert(question.getResultList().get(3).getValue0().equals(new String(Character.toChars(172))));
        assert(question.getResultList().get(3).getValue1().equals("FT"));
        assert(question.getResults().equals("TT"));
        assert(question.getFinalAnswer().equals("contradiction"));

        //Contingent test
        question = parser.parseQuestion("A^B");
        assert(question.getResultList().get(1).getValue0().equals("^"));
        assert(question.getResultList().get(1).getValue1().equals("TFFF"));
        assert(question.getResults().equals("FTTT"));
        assert(question.getFinalAnswer().equals("contingent"));

        //NOTE: Parser cannot currently handle duplicate pieces in a question
        //example: A v B :: A v B
        //example: A v (~B), ~B
        //Duplicate letters are ok though
        //example: A v B, A
        //example: A :: A
    }
}
