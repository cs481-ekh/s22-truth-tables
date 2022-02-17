package com.bsu.truthtables.dao;

import com.bsu.truthtables.domain.Question;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.Collections;
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

    public List<Integer> getChapters() {
        List<Integer> list = getSqlSession().selectList("getChapters");
        Collections.sort(list);
        return list;
    }
    public void removeQuestion(String question, int chapter) {
        Map<String, String> params = new HashMap<>();
        params.put("chapter", String.valueOf(chapter));
        params.put("question", question);
        getSqlSession().delete("removeQuestion", params);
    }
}
