package com.bsu.truthtables.domain;

import lombok.Data;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ParsedQuestion {

    //Will remove once I change the implementation in the Parser, but for now they need to stay
    //so they don't break the build
    private ArrayList<Pair<String, String>> resultList;

    //Front end will iterate through each statement in this list in order to grab
    //that statement's resultList
    private ArrayList<ArrayList<Pair<String, String>>> statementList;

    //List of last column values for each problem type
    private String showsNotTautology;
    private String showsEquivalent;
    private String showsConsistent;
    private String showsInvalid;

    //Problem types
    private boolean argument = false;
    private boolean consistency = false;
    private boolean equivalence = false;
    private boolean logical = false;

    //Final answer values
    private boolean isTautology = false;
    private boolean isContradiction = false;
    private boolean isEquivalent = false;
    private boolean isConsistent = false;
    private boolean isValid = false;

    private Map<String,String> map;
}
