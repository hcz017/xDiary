package com.training.hcz017.xdiary.article;

public class ArticleBean {
    private String mTitle;
    private String mTime;
    private String mContent;
    private String mLink;
    private String mImgUrl;
    public static final String TITLE = "title";
    public static final String LINK = "link";

    public ArticleBean(String mTitle, String mTime, String mContent, String link, String imgUrl) {
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mContent = mContent;
        this.mLink = link;
        this.mImgUrl = imgUrl;
    }

    public ArticleBean(String mTitle, String date, String link) {
        this.mTitle = mTitle;
        this.mTime = date;
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

    public String getImgUrl() {
        return mImgUrl;
    }
}
