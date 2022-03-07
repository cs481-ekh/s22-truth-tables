package com.bsu.truthtables;

import com.bsu.truthtables.parser.Parser;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TruthtablesApplicationTests {

	@Autowired
	Parser parser;

	@Test
	void contextLoads() {
	}

	@Test
	void basicEval(){
		String question1 = "A ^ B";
		String question2 = "A v B";
		String question3 = "! B";
		String question4 = "A v B v C";
		String question5 = "A ^ B ^ C";
		String question6 = "! ! A";
		String question7 = "! ! ! ! !A";
		String question8 = "A^B";
		String question9 = "(A^B)";
		String question10 = "(A^B)vc";
		String question11 = "(AvB) ^c";

		List q1 =parser.parseQuestion(question1);
        List q2 =parser.parseQuestion(question2);
        List q3 =parser.parseQuestion(question3);
        List q4 =parser.parseQuestion(question4);
        List q5 =parser.parseQuestion(question5);
        List q6 =parser.parseQuestion(question6);
        List q7 =parser.parseQuestion(question7);
        List q8 =parser.parseQuestion(question8);
        List q9 =parser.parseQuestion(question9);
        List q10 =parser.parseQuestion(question10);
        List q11 =parser.parseQuestion(question11);

		//todo add assertions
		System.out.println(q1.toString());
		System.out.println(q2.toString());
		System.out.println(q3.toString());
		System.out.println(q4.toString());
		System.out.println(q5.toString());
		System.out.println(q6.toString());
		System.out.println(q7.toString());
		System.out.println(q8.toString());
		System.out.println(q9.toString());
		System.out.println(q10.toString());
		System.out.println(q11.toString());
	}

	@Test
	void arrowStuff(){
		String question1 = "A ^ B -> C";		//idk if this is right, I was working off the idea -> means if left is true then right is true
		String question2 = "A ^ B <-> C";		//stmt only true if both are true
        List q1 =parser.parseQuestion(question1);
        List q2 =parser.parseQuestion(question2);

		//todo add assertions
		System.out.println(q1.toString());
		System.out.println(q2.toString());
	}

	@Test
	void kyleStuff() {
		String question = "A v (B ->C)";
        List q1 =parser.parseQuestion(question);
		ArrayList<Pair<String, String>> data = parser.getData();
		System.out.println(question);
		System.out.println(q1.toString());
		System.out.println(data.toString());

		System.out.println();

		String question2 = "!((!A v B) ^ C)";
        List q2 =parser.parseQuestion(question2);
		data = parser.getData();
		System.out.println(question2);
		System.out.println(q2.toString());
		System.out.println(data.toString());

//		String question3 = "A v (B v (C v D))";
//        List q3 =parser.parseQuestion(question3);
//		data = parser.getData();
//		ops = data.get(0);
//		values = data.get(1);
//		System.out.println(question3);
//		System.out.println(q3.toString());
//		System.out.println(ops.toString());
//		System.out.println(values.toString());
	}
}
