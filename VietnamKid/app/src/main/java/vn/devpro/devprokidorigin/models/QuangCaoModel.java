package vn.devpro.devprokidorigin.models;

/**
 * Created by hienc on 5/26/2018.
 */

public class QuangCaoModel {
    private int linkAnh;
    private String tenGame;
    private String Mota;
    private String soSao;



    public QuangCaoModel(int linkAnh, String tenGame, String Mota, String soSao) {
        this.linkAnh = linkAnh;
        this.tenGame = tenGame;
        this.Mota = Mota;
        this.soSao = soSao;

    }

    public int getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(int linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getTenGame() {
        return tenGame;
    }

    public void setTenGame(String tenGame) {
        this.tenGame = tenGame;
    }

    public String getSoSao() {
        return soSao;
    }

    public void setSoSao(String soSao) {
        this.soSao = soSao;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }
}
