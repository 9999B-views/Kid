package vn.devpro.devprokidorigin.models.xephinh;

public class GameModel {
    public GameModel(int pPieceNumbers, int pImageID) {
        pieceNumbers = pPieceNumbers;
        imageID = pImageID;
    }

    private int pieceNumbers;

    public int getPieceNumbers() {
        return pieceNumbers;
    }

    public int imageID;
}
