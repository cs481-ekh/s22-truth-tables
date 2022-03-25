package com.bsu.truthtables.domain;

import lombok.Data;
import org.javatuples.Pair;

import java.util.ArrayList;

@Data
public class Statement {

    //This should be the only value the front-end needs to access from the Statement class
    private ArrayList<Pair<String, String>> resultList;

    //The rest of these are being used by the Parser
    private String statement;
    private String values;
    private boolean isPremise = false;
    private boolean isConclusion = false;
    private boolean isTautology = false;
    private boolean isContingent = false;
}
