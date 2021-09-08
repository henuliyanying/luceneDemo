package com.example.mybatisdemo.service.impl;

import com.example.mybatisdemo.eneities.Question;
import com.example.mybatisdemo.mapper.QuestionMapper;
import com.example.mybatisdemo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:49
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> getAllQuestion() {
        List<Question> questions = questionMapper.getAllQuestion();
        return questions;
    }
}
