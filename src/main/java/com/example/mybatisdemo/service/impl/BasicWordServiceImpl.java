package com.example.mybatisdemo.service.impl;

import com.example.mybatisdemo.eneities.BasicWord;

import com.example.mybatisdemo.eneities.WordStage;
import com.example.mybatisdemo.mapper.BasicWordMapper;
import com.example.mybatisdemo.service.BasicWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 10:12
 */
@Service
public class BasicWordServiceImpl implements BasicWordService {
    @Autowired
    private BasicWordMapper basicWordMapper;

    @Override
    public List<BasicWord> getAllBasicWord(){
        List<BasicWord> allBasicWord = basicWordMapper.getAllBasicWord();
        return allBasicWord;
    }

    @Override
    public List<BasicWord> getWfreBySpellAndStage(){
        List<BasicWord> wfreBySpellAndStage = basicWordMapper.getWfreBySpellAndStage();
        return  wfreBySpellAndStage;
    }

//    @Override
//    public BasicWord getWfreBySpellAndStage(){
//        BasicWord wfreBySpellAndStage = basicWordMapper.getWfreBySpellAndStage();
//        return  wfreBySpellAndStage;
//    }


}
