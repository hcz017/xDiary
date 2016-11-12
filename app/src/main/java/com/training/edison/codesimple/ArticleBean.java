package com.training.edison.codesimple;

public class ArticleBean {
    private String mTitle;
    private String mTime;
    private String mContent;
    private String mLink;
    public static final String TITLE = "title";
    public static final String LINK = "link";

    public ArticleBean(String mTitle, String mTime, String mContent, String link) {
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mContent = mContent;
        this.mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public String getContent() {
        return mContent;
    }

    public String getLink() {
        return mLink;
    }
}
