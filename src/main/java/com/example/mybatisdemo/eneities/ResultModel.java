package com.example.mybatisdemo.eneities;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @team 魔法城堡
 * @date 2021/3/17 15:35
 */
public class ResultModel {
    // 商品列表
    private List<Question> questionList;
    // 商品总数
    private Long recordCount;
    // 总页数
    private Long pageCount;
    // 当前页
    private long curPage;

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }
}
