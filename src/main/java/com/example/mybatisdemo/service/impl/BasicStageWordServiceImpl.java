package com.example.mybatisdemo.service.impl;

import com.example.mybatisdemo.eneities.BasicStageWord;
import com.example.mybatisdemo.mapper.BasicStageWordMapper;
import com.example.mybatisdemo.mapper.BasicWordMapper;
import com.example.mybatisdemo.service.BasicStageWordService;
import com.example.mybatisdemo.service.BasicWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/23 8:58
 */
@Service
public class BasicStageWordServiceImpl implements BasicStageWordService {
    @Autowired
    private BasicStageWordMapper basicStageWordMapper;

    @Override
    public List<BasicStageWord> getAllWordDetail(){
        List<BasicStageWord> basicStageWords = basicStageWordMapper.getAllWordDetail();
        return basicStageWords;
    }
}
