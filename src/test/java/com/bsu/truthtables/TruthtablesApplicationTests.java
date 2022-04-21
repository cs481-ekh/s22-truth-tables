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
    }
}
