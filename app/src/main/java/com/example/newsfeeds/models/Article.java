package com.example.newsfeeds.models;

import com.example.newsfeeds.util.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName(Constant.ARTICLE_SOURCE)
    @Expose
    private Source source;

    @SerializedName(Constant.ARTICLE_AUTHOR)
    @Expose
    private String author;

    @SerializedName(Constant.ARTICLE_TITTLE)
    @Expose
    private String title;

    @SerializedName(Constant.ARTICLE_DESCRIPTION)
    @Expose
    private String description;

    @SerializedName(Constant.ARTICLE_URL)
    @Expose
    private String url;

    @SerializedName(Constant.ARTICLE_URL_TO_IMAGE)
    @Expose
    private String urlToImage;

    @SerializedName(Constant.ARTICLE_PUBLISHED_AT)
    @Expose
    private String publishedAt;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
