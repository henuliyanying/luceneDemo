package com.example.mybatisdemo.service.impl;

import com.example.mybatisdemo.mapper.WordStageMapper;
import com.example.mybatisdemo.service.WordStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mybatisdemo.eneities.WordStage;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 9:12
 */
@Service
public class WordStageServiceImpl implements WordStageService {

    @Autowired
    private WordStageMapper wordStageMapper;
    //获取所有题库
    @Override
    public List<WordStage> getAllWordStage(){
        List<WordStage> allWordStage = wordStageMapper.getAllWordStage();
        return allWordStage;
    }
}
