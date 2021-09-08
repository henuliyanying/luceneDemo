package com.example.mybatisdemo.service;

import com.example.mybatisdemo.eneities.Question;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:47
 */
public interface QuestionService {
    //获取所有题库
    List<Question> getAllQuestion();
}
