package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.eneities.StuQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:38
 */
@Mapper
@Repository
public interface StuQuestionMapper {
    //根据学生ID获取题库编号q_num
    List<StuQuestion> getQuestionByS_num(String s_num);
}
