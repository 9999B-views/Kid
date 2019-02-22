package vn.devpro.devprokidorigin.models.entity.hocchucai;

/**
 * Created by hoang-ubuntu on 20/03/2018.
 */

public class Question {
    private int id;
    private String title_vn;
    private String title_en;
    private String sound_vn;
    private String sound_en;
    private int show_ratio;
    private int correct_count;

    public Question(){}
    
    public Question(int id, String title_vn, String title_en, String sound_vn, String sound_en, int show_ratio, int correct_count) {
        this.id = id;
        this.title_vn = title_vn;
        this.title_en = title_en;
        this.sound_vn = sound_vn;
        this.sound_en = sound_en;
        this.show_ratio = show_ratio;
        this.correct_count = correct_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
