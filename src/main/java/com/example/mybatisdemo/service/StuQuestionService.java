package com.example.mybatisdemo.service;

import com.example.mybatisdemo.eneities.StuQuestion;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:48
 */
public interface StuQuestionService {
    //根据学生ID获取题库编号q_num
    List<StuQuestion> getQuestionByS_num(String s_num);
}
