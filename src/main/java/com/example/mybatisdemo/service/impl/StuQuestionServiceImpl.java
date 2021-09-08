package com.example.mybatisdemo.service.impl;

import com.example.mybatisdemo.eneities.StuQuestion;
import com.example.mybatisdemo.mapper.StuQuestionMapper;
import com.example.mybatisdemo.service.StuQuestionService;
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
public class StuQuestionServiceImpl implements StuQuestionService {
    @Autowired
    private StuQuestionMapper stuQuestionMapper;

    @Override
    public List<StuQuestion> getQuestionByS_num(String s_num) {
        return stuQuestionMapper.getQuestionByS_num(s_num);
    }
}
