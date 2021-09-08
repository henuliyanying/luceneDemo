package com.example.mybatisdemo.eneities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuQuestion {
    private Integer id;
    private  String s_num;
    private String Questions;
}
