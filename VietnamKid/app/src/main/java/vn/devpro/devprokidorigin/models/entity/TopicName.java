package vn.devpro.devprokidorigin.models.entity;

/**
 * Created by admin on 3/23/2018.
 */

public class TopicName {
    private int id;
    private int index = 100; // for ordering item when showing on UI view
    private String title_vn, title_en;
    private String img;
    private int percent;
    private int lock;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] pImageData) {
        imageData = pImageData;
    }

    private byte[] imageData = null;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int pIndex) {
        index = pIndex;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public TopicName(int id, String title_vn, String title_en, String img, int percent, int lock) {
        this.id = id;
        this.title_vn = title_vn;
        this.title_en = title_en;
        this.img = img;
        this.percent = percent;
        this.lock = lock;
    }

}
