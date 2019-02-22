package vn.devpro.devprokidorigin.models.entity;

import android.graphics.Bitmap;

/**
 * Created by Laptop88 on 3/17/2018.
 */

public class TopicItem {
    private byte[] bitImg = null;
    private Bitmap bitmap;
    private int id;
    private int topic_id;
    private String title_vn,title_en, img_name, sound_vn, sound_en;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private int show_ratio;
    private int correct_count;

    public TopicItem(int id, String title_vn,
                     String title_en, String img_name, String sound_vn,
                     String sound_en, int show_ratio, int correct_count) {
        this.id = id;
        this.title_vn = title_vn;
        this.title_en = title_en;
        this.img_name = img_name;
        this.sound_vn = sound_vn;
        this.sound_en = sound_en;
        this.show_ratio = show_ratio;
        this.correct_count = correct_count;
    }

    public byte[] getBitImg() {
        return bitImg;
    }

    public void setBitImg(byte[] bitImg) {
        this.bitImg = bitImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getTitle_vn() {
        return title_vn;
    }

    public void setTitle_vn(String title_vn) {
        this.title_vn = title_vn;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getSound_vn() {
        return sound_vn;
    }

    public void setSound_vn(String sound_vn) {
        this.sound_vn = sound_vn;
    }

    public String getSound_en() {
        return sound_en;
    }

    public void setSound_en(String sound_en) {
        this.sound_en = sound_en;
    }

    public int getShow_ratio() {
        return show_ratio;
    }

    public void setShow_ratio(int show_ratio) {
        this.show_ratio = show_ratio;
    }

    public int getCorrect_count() {
        return correct_count;
    }

    public void setCorrect_count(int correct_count) {
        this.correct_count = correct_count;
    }
}
