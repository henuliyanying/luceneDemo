package com.example.mybatisdemo.service;

import com.example.mybatisdemo.eneities.WordStage;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 9:07
 */
public interface WordStageService {
    //获取所有题库
    List<WordStage> getAllWordStage();
}
