package com.alpay.wesapiens.models;

public class Frame {

    int frameID;
    String frameName;
    String frameTime;
    String framePlace;
    String frameStartImage;
    String frameEndImage;
    String frameContext;
    String frameQuestion;
    String frameAnswer;

    public Frame() {

    }

    public Frame(int frameID, String frameName, String frameTime, String framePlace, String frameStartImage, String frameEndImage, String context, String question, String answer) {
        this.frameID = frameID;
        this.frameName = frameName;
        this.frameTime = frameTime;
        this.framePlace = framePlace;
        this.frameStartImage = frameStartImage;
        this.frameEndImage = frameEndImage;
        this.frameContext = context;
        this.frameQuestion = question;
        this.frameAnswer = answer;
    }

    public int getFrameID() {
        return frameID;
    }

    public void setFrameID(int frameID) {
        this.frameID = frameID;
    }

    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }

    public String getFrameStartImage() {
        return frameStartImage;
    }

    public String getFrameEndImage() {
        return frameEndImage;
    }

    public void setFrameEndImage(String frameEndImage) {
        this.frameEndImage = frameEndImage;
    }

    public void setFrameStartImage(String frameStartImage) {
        this.frameStartImage = frameStartImage;
    }

    public String getFrameAnswer() {
        return frameAnswer;
    }

    public void setFrameAnswer(String frameAnswer) {
        this.frameAnswer = frameAnswer;
    }

    public String getFrameContext() {
        return frameContext;
    }

    public void setFrameContext(String frameContext) {
        this.frameContext = frameContext;
    }

    public String getFrameQuestion() {
        return frameQuestion;
    }

    public void setFrameQuestion(String frameQuestion) {
        this.frameQuestion = frameQuestion;
    }

    public String getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(String frameTime) {
        this.frameTime = frameTime;
    }

    public String getFramePlace() {
        return framePlace;
    }

    public void setFramePlace(String framePlace) {
        this.framePlace = framePlace;
    }
}
