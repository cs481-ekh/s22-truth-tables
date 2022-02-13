package com.bsu.truthtables.controller;

import com.bsu.truthtables.dao.Dao;
import com.bsu.truthtables.domain.Question;
import com.bsu.truthtables.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private Dao dao;

    @Autowired
    private Parser parser;

    @Value("#{${prefilledBox}}")
    private Map<String,String> prefilledBox;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("question" , new Question());
        model.addAttribute("chapters", dao.getChapters());
        return "home";
    }

    @PostMapping("/")
    public String practiceProblem(@ModelAttribute Question question, Model model) {
        int numberOfInputs = parser.parseChars(question.getQuestion()).length();
        int boxDepth = (int) Math.round(Math.pow(2,numberOfInputs));
        String chars = parser.parseChars(question.getQuestion());
        model.addAttribute("question", question);
        model.addAttribute("chapters", dao.getChapters());
        model.addAttribute("prefilled", prefilledBox.get(String.valueOf(numberOfInputs)));
        model.addAttribute("numberOfInputs", numberOfInputs);
        model.addAttribute("inputChars", chars);
        model.addAttribute("boxDepth", boxDepth);

        return "practice-problem";
    }
    @PostMapping("/chapter-questions")
    public String getChapterQuestions(@ModelAttribute Question question, Model model) {
        //this is where we would grade and check stuff then load resulting page
        return "graded";
    }
    @GetMapping("/chapter-questions/{chapter}")
    public String getChapterQuestions2(@PathVariable("chapter") int chapter, Model model) {
        model.addAttribute("chapter", chapter);
        model.addAttribute("listOfQuestions", dao.getAllByChapter(chapter));
        return "chapter-questions";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("question" , new Question());
        return "admin";
    }

    @PostMapping("/admin")
    public String adminSubmit(@ModelAttribute Question question, Model model) {
        int numberOfInputs = parser.parseChars(question.getQuestion()).length();
        int boxDepth = (int) Math.round(Math.pow(2,numberOfInputs));
        String chars = parser.parseChars(question.getQuestion());
        model.addAttribute("question", question);
        model.addAttribute("listOfQuestions", dao.getAllByChapter(Integer.parseInt(question.getChapter())));
        model.addAttribute("prefilled", prefilledBox.get(String.valueOf(numberOfInputs)));
        model.addAttribute("numberOfInputs", numberOfInputs);
        model.addAttribute("inputChars", chars);
        model.addAttribute("boxDepth", boxDepth);
        dao.add(question.getQuestion(), question.getChapter(), chars);
        return "admin-result";
    }
}
