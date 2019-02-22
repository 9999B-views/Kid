package com.alpay.codenotes.models;

public class Feedback {
    private String title;
    private String detail;

    public Feedback(String title, String detail) {
        this.title = title;
        this.detail = detail;

    }

    public Feedback() {

    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
