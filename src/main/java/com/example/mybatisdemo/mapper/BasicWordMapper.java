package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.eneities.BasicWord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 10:10
 */
@Mapper
@Repository
public interface BasicWordMapper {
    //List<WordStage> getAllWordStage();

    List<BasicWord> getAllBasicWord();

    List<BasicWord> getWfreBySpellAndStage();
}
