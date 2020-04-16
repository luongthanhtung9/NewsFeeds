package com.example.newsfeeds.models;

import com.example.newsfeeds.util.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    @SerializedName(Constant.SOURCE_ID)
    @Expose
    private String id;

    @SerializedName(Constant.SOURCE_NAME)
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
