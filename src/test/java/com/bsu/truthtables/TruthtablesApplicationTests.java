package com.bsu.truthtables;

import com.bsu.truthtables.parser.Parser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

		Map q1 =parser.parseQuestion(question1);
		Map q2 =parser.parseQuestion(question2);
		Map q3 =parser.parseQuestion(question3);
		Map q4 =parser.parseQuestion(question4);
		Map q5 =parser.parseQuestion(question5);
		Map q6 =parser.parseQuestion(question6);
		Map q7 =parser.parseQuestion(question7);
		Map q8 =parser.parseQuestion(question8);
		Map q9 =parser.parseQuestion(question9);
		Map q10 =parser.parseQuestion(question10);
		Map q11 =parser.parseQuestion(question11);

		//todo add assertions
		System.out.println("stop");
	}

}
