package com.example.mybatisdemo.service;

import com.example.mybatisdemo.eneities.BasicStageWord;
import com.example.mybatisdemo.mapper.BasicStageWordMapper;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/23 8:58
 */
public interface BasicStageWordService {
    List<BasicStageWord> getAllWordDetail();
}
