package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.eneities.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:37
 */
@Mapper
@Repository
public interface QuestionMapper {
    //获取所有题库
    List<Question> getAllQuestion();
}
