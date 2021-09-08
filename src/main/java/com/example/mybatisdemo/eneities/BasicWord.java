package com.example.mybatisdemo.eneities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 10:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicWord {
    private String w_id;
    private String w_spell;
    private String w_type;
    private String w_mean;
    private String w_pronunce;
//    private int isDeleted;
//    private String remarks;
    private WordStage wordStage;
}
