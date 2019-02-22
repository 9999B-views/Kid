package vn.devpro.devprokidorigin.models;

/**
 * Created by hienc on 4/13/2018.
 */

public class TroChoiModel {
    private String gameNameVN;
    private String gameNameEN;
    private int id;
    //private int AnhNen;

/*    public int getAnhNen() {
        return AnhNen;
    }

    public void setAnhNen(int anhNen) {
        AnhNen = anhNen;
    }*/

    public TroChoiModel(int id, String pGameNameVN, String pGameNameEN) {
        this.gameNameVN = pGameNameVN;
        this.gameNameEN = pGameNameEN;
        this.id = id;
    }

    public String getGameNameVN() {
        return gameNameVN;
    }

    public void setGameNameVN(String pGameNameVN) {
        gameNameVN = pGameNameVN;
    }

    public String getGameNameEN() {
        return gameNameEN;
    }

    public void setGameNameEN(String pGameNameEN) {
        gameNameEN = pGameNameEN;
    }

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        id = pId;
    }
}
