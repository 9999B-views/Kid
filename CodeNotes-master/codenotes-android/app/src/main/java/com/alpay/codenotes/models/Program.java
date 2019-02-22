package com.alpay.codenotes.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.File;

public class Program {

    String programName = "Program Name";
    String[] programText = {"Program Text"};
    String[] programImages = {"ImageList"};

    public Program() {

    }

    public Program(String programName, String[] programText, String[] programImages) {
        this.programName = programName;
        this.programText = programText;
        this.programImages = programImages;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String[] getProgramText() {
        return programText;
    }

    public void setProgramText(String[] programText) {
        this.programText = programText;
    }

    public String[] getProgramImages() {
        return programImages;
    }

    public void setProgramImages(String[] programImages) {
        this.programImages = programImages;
    }

    public Bitmap getThumbnailImage(Context context, String imagePath) {
        File f = new File(imagePath);
        return new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
    }
}
