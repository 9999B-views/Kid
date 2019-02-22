package vn.devpro.devprokidorigin.models.entity.hocchucai;

/**
 * Created by HOANG on 3/13/2018.
 */

public class LetterModelSmall {
    private String image_name;
    private int idSmall;

    public int getIndex() {
        return mIndex;
    }

    private int mIndex;

    public LetterModelSmall(String image_name, int idSmall) {
        this.image_name = image_name;
        this.idSmall = idSmall;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getId() {
        return idSmall;
    }

    public void setId(int id) {
        this.idSmall = id;
    }

    public void setIndex(int pIndex) {
        mIndex = pIndex;
    }
}
