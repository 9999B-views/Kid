package vn.devpro.devprokidorigin.models.entity.game;

/**
 * Created by hoang-ubuntu on 04/04/2018.
 */

public class PointDraw {
    private float xLocate;
    private float yLocate;
    private String tagImage;

    public PointDraw() {
        xLocate = -1;
        yLocate = -1;
        tagImage = "";
    }

    public PointDraw(float xLocate, float yLocate, String tagImage) {
        this.xLocate = xLocate;
        this.yLocate = yLocate;
        this.tagImage = tagImage;
    }

    public float getxLocate() {
        return xLocate;
    }

    public void setxLocate(float xLocate) {
        this.xLocate = xLocate;
    }

    public float getyLocate() {
        return yLocate;
    }

    public void setyLocate(float yLocate) {
        this.yLocate = yLocate;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }
}
