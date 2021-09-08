package com.example.mybatisdemo.eneities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/6/22 9:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordStage {
    public String getW_id() {
        return w_id;
    }

    public void setW_id(String w_id) {
        this.w_id = w_id;
    }

    public String getW_stage() {
        return w_stage;
    }

    public void setW_stage(String w_stage) {
        this.w_stage = w_stage;
    }

    public String getW_frequency() {
        return w_frequency;
    }

    public void setW_frequency(String w_frequency) {
        this.w_frequency = w_frequency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String w_id;
    private String w_stage;
//    private String w_times;
    private String w_frequency;
//    private String w_creater;
//    private String create_time;
//    private String w_checker;
//    private String check_time;

}
