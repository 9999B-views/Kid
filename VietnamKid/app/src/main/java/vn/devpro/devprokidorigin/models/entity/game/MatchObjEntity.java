package vn.devpro.devprokidorigin.models.entity.game;

/**
 * Created by hoang-ubuntu on 03/05/2018.
 */

public class MatchObjEntity {
    private int id;
    private String img_name;
    private int pair;
    private int count;
    private String letter_vn;
    private String letter_en;

    public MatchObjEntity() {
    }

    public MatchObjEntity(int id, String img_name, int pair,
                          int count, String letter_vn, String letter_en) {
        this.id = id;
        this.img_name = img_name;
        this.pair = pair;
        this.count = count;
        this.letter_vn = letter_vn;
        this.letter_en = letter_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public int getPair() {
        return pair;
    }

    public void setPair(int pair) {
        this.pair = pair;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLetter_vn() {
        return letter_vn;
    }

    public void setLetter_vn(String letter_vn) {
        this.letter_vn = letter_vn;
    }

    public String getLetter_en() {
        return letter_en;
    }

    public void setLetter_en(String letter_en) {
        this.letter_en = letter_en;
    }
}
