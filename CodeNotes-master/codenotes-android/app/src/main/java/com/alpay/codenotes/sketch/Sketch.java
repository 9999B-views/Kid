package com.alpay.codenotes.sketch;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import processing.core.PApplet;

public class Sketch extends PApplet {

    int width;
    int height;
    QuickDraw qd;
    float end, x;
    String[] dataFile;

    public Sketch(AppCompatActivity appCompatActivity){
        Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        dataFile = readFile(appCompatActivity, "monkey.ndjson");
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        qd = new QuickDraw(this, dataFile);
    }

    public void draw() {
        background(255);
        float scale = height/2;
        endUpdate();
        qd.create(width/2, height/2, scale, scale, end);
    }

    void endUpdate() {
        float inc = .01f;
        end = abs(sin(x));
        x = x + inc;
    }

    public String[] readFile(AppCompatActivity appCompatActivity, String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(appCompatActivity.getAssets().open(fileName)));

            String lines[] = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    String temp[] = new String[lineCount << 1];
                    System.arraycopy(lines, 0, temp, 0, lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();

            if (lineCount == lines.length) {
                return lines;
            }

            // resize array to appropriate amount for these lines
            String output[] = new String[lineCount];
            System.arraycopy(lines, 0, output, 0, lineCount);
            return output;

        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException("Error inside loadStrings()");
        }
        return null;
    }
}