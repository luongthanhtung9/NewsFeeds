package com.example.newsfeeds.models;

import com.example.newsfeeds.util.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName(Constant.NEWS_STATUS)
    @Expose
    private String status;

    @SerializedName(Constant.NEWS_TOTAL_RESULT)
    @Expose
    private int totalResult;

    @SerializedName(Constant.NEWS_ARTICLES)
    @Expose
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
