package vn.devpro.devprokidorigin.models;

/**
 * Created by hienc on 4/14/2018.
 */

public class ItemProperty {
    public  int width,height;
    public int left_right_margin,top_bottom_margin;

    public ItemProperty(int width, int height, int left_right_margin, int top_bottom_margin) {
        this.width = width;
        this.height = height;
        this.left_right_margin = left_right_margin;
        this.top_bottom_margin = top_bottom_margin;
    }
}
