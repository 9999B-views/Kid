package vn.devpro.devprokidorigin.models.xephinh;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public class PuzzlePiece extends AppCompatImageView {
    public int xCoord;
    public int yCoord;
    public int xStartCoord;
    public int yStartCoord;
    public int startWidth;
    public int startHeight;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;
    public int index = 0;

    public PuzzlePiece(Context context) {
        super(context);
    }
}
