package vn.devpro.devprokidorigin.models.entity.game;

public class FindShadow {
    int id, topic_id;
    String origin_img, shadow_img, sound_vn, sound_en,tag;


    public FindShadow(int id, String origin_img, String shadow_img, String sound_vn, String sound_en) {
        this.id = id;
        this.origin_img = origin_img;
        this.shadow_img = shadow_img;
        this.sound_vn = sound_vn;
        this.sound_en = sound_en;
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

    public String getOrigin_img() {
        return origin_img;
    }

    public void setOrigin_img(String origin_img) {
        this.origin_img = origin_img;
    }

    public String getShadow_img() {
        return shadow_img;
    }

    public void setShadow_img(String shadow_img) {
        this.shadow_img = shadow_img;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
