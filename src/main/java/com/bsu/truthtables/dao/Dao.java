package com.bsu.truthtables.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dao extends SqlSessionDaoSupport {

    public void add(String question, String chapter, String chars) {
        Map<String, String> params = new HashMap<>();
        params.put("chapter", chapter);
        params.put("question", question);
        params.put("chars", chars);
        getSqlSession().insert("add", params); //todo drop test table and change chars to be varchar
    }
    public List<String> getAllByChapter(int chapter){
        return getSqlSession().selectList("getAllByChapter", chapter);
    }
}
