package com.bsu.truthtables.domain;

import lombok.Data;
import org.javatuples.Pair;

import java.util.ArrayList;

@Data
public class ParsedQuestion {

    //Front end will iterate through each statement in this list in order to grab
    //that statement's resultList
    private ArrayList<ArrayList<Pair<String, String>>> statementList;

    //List of last column values for each problem type
    private ArrayList<String> showsNotTautology;
    private ArrayList<String> showsEquivalent;
    private ArrayList<String> showsConsistent;
    private ArrayList<String> showsInvalid;

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
}
