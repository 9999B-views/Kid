package com.alpay.codenotes.models;

public class Output {
    private String outputText;
    private String filePath;

    public Output(String outputText, String filePath) {
        this.outputText = outputText;
        this.filePath = filePath;
    }

    public Output(){

    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public String getEncodedImage() {
        return filePath;
    }

    public void setEncodedImage(String encodedImage) {
        this.filePath = encodedImage;
    }
}
