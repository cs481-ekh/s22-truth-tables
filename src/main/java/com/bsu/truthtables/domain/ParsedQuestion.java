package com.bsu.truthtables.domain;

import lombok.Data;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsedQuestion {

    private String validity = "";
    private ArrayList<Pair<String, String>> resultList;
    private boolean argument = false;
    private boolean consistency = false;
    private boolean equivalence = false;
    private boolean logical = false;
}
