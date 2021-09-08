package com.example.mybatisdemo.service;

import com.example.mybatisdemo.eneities.BasicWord;
import com.example.mybatisdemo.eneities.WordStage;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 10:11
 */
public interface BasicWordService {
    List<BasicWord> getAllBasicWord();

    List<BasicWord >getWfreBySpellAndStage();
}
