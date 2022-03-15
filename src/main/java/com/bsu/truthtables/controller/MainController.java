package com.bsu.truthtables.controller;

import com.bsu.truthtables.dao.Dao;
import com.bsu.truthtables.domain.Question;
import com.bsu.truthtables.domain.Submission;
import com.bsu.truthtables.parser.Parser;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private Dao dao;

    @Autowired
    private Parser parser;

    @Value("#{${prefilledBox}}")
    private Map<String, String> prefilledBox;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("chapters", dao.getChapters());
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("chapters", dao.getChapters());

        return "home";
    }

    @PostMapping("/")
    public String practiceProblem(@ModelAttribute Question question, Model model, RedirectAttributes ra) {
        int numberOfInputs = parser.parseChars(question.getQuestion()).length();
        int boxDepth = (int) Math.round(Math.pow(2, numberOfInputs));
        ArrayList<Pair<String, String>> results = parser.parseQuestion(question.getQuestion());
        String chars = parser.parseChars(question.getQuestion());
        model.addAttribute("question", question);
        model.addAttribute("chapters", dao.getChapters());
        model.addAttribute("prefilled", prefilledBox.get(String.valueOf(numberOfInputs)));
        model.addAttribute("numberOfInputs", numberOfInputs);
        model.addAttribute("inputChars", chars);
        model.addAttribute("boxDepth", boxDepth);
        model.addAttribute("pageTitle", "Problem");
        model.addAttribute("results", results);
        model.addAttribute("submit", new Submission(parser.orderResults(results)));
        return "practice-problem";
    }

    @PostMapping("/chapter-questions")
    public String getChapterQuestions(@ModelAttribute Question question, Model model) {
        //this is where we would grade and check stuff then load resulting page
        return "graded";
    }

    @GetMapping("/chapter/{chapter}")
    public String getChapterQuestions2(@PathVariable("chapter") int chapter, Model model) {
        model.addAttribute("chapter", chapter);
        model.addAttribute("listOfQuestions", dao.getAllByChapter(chapter));
        model.addAttribute("pageTitle", "Chapter Questions");
        model.addAttribute("chapters", dao.getChapters());

        return "chapter-questions";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("pageTitle", "Admin");
        model.addAttribute("chapters", dao.getChapters());
        return "admin";
    }

    @PostMapping("/admin")
    public String adminSubmit(@ModelAttribute Question question, Model model) {

        int numberOfInputs = parser.parseChars(question.getQuestion()).length();
        int boxDepth = (int) Math.round(Math.pow(2, numberOfInputs));
        ArrayList<Pair<String, String>> results = parser.parseQuestion(question.getQuestion());
        String chars = parser.parseChars(question.getQuestion());
        model.addAttribute("question", question);
        model.addAttribute("chapters", dao.getChapters());
        model.addAttribute("prefilled", prefilledBox.get(String.valueOf(numberOfInputs)));
        model.addAttribute("numberOfInputs", numberOfInputs);
        model.addAttribute("inputChars", chars);
        model.addAttribute("boxDepth", boxDepth);
        model.addAttribute("pageTitle", "Problem");
        model.addAttribute("results", results);
        model.addAttribute("submit", new Submission(parser.orderResults(results)));
        model.addAttribute("pageTitle", "Admin");
        dao.add(question.getQuestion(), question.getChapter(), chars);
        return "admin-result";
    }

    @GetMapping("/removeQuestion/{question}/{chapter}")
    public String adminDelete(@PathVariable String question, @PathVariable int chapter, Model model) {
        dao.removeQuestion(question, chapter);
        model.addAttribute("listOfQuestions", dao.getAllByChapter(chapter));
        return manage(model);
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        HashMap map = new HashMap();
        for (int chapter : dao.getChapters()) {
            map.put(chapter, dao.getAllByChapter(chapter));

        }
        model.addAttribute("map", map);
        model.addAttribute("pageTitle", "manage");

        return "manage";
    }
}
