package com.bsu.truthtables.controller;

import com.bsu.truthtables.dao.Dao;
import com.bsu.truthtables.domain.Question;
import com.bsu.truthtables.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private Dao dao;

    @Autowired
    private Parser parser;

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("question" , new Question());
        return "add";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute Question question, Model model) {
        model.addAttribute("question", question);
        dao.add(question.getQuestion(), question.getChapter(), parser.parseChars(question.getQuestion()));
        Object obj = parser.parseQuestion(question.getQuestion());
        model.addAttribute("listOfQuestions", dao.getAllByChapter(Integer.parseInt(question.getChapter())));
        return "result";
    }
}
