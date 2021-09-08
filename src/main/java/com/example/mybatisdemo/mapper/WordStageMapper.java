package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.eneities.WordStage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 9:13
 */
@Mapper
@Repository
public interface WordStageMapper {

    List<WordStage> getAllWordStage();
//     WordStage  getAllWordStage();
}
