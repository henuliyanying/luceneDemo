package com.example.mybatisdemo.eneities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    //题型编号
    private String q_num;
    //题库主键id
    private Integer id;
    //题库类别：1词汇；2语法
    private String q_single_type;
    //是否为真题：1真题； 0非真题
    private Integer q_isreal;
    //题目年级：1小学；2初中；3高中
    private String q_stage;
    //题目内容
    private String q_content;
    //题目难度范围
    private Integer q_difficulty;
    //题目creater
    private String q_creater;
}
