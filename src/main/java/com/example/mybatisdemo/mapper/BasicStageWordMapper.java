package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.eneities.BasicStageWord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/23 8:56
 */
@Mapper
@Repository
public interface BasicStageWordMapper {
    List<BasicStageWord> getAllWordDetail();
}
