package com.example.nandhu.alphabets;

import static android.R.attr.name;

/**
 * Created by Nandhu on 09-03-2017.
 */

public class Alphabets {
     int  Img;
    String Name;
    int Audio;

    public Alphabets(int img, String name,int audio) {
        Img = img;
        Name = name;
        Audio = audio;
    }

    public int getAudio() {
        return Audio;
    }

    public int getImg() {
        return Img;
    }

    public String getName() {
        return Name;
    }
}
