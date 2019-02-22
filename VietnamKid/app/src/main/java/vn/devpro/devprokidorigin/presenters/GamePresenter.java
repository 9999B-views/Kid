package vn.devpro.devprokidorigin.presenters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.controllers.game.GameProvider;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.game.MatchObjEntity;
import vn.devpro.devprokidorigin.models.entity.game.PointDraw;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by NamQuoc on 4/23/2018.
 */

public class GamePresenter {
    private final String TAG = GamePresenter.class.getSimpleName();
    private List<MatchObjEntity> listObjAll;
    private int[] pos1 = new int[2];
    private int[] pos2 = new int[2];
    private int[] pos3 = new int[2];
    private int[] pos4 = new int[2];
    private int[] pos5 = new int[2];
    private int[] pos6 = new int[2];

    public GamePresenter(int[] pos1, int[] pos2, int[] pos3, int[] pos4,
                         int[] pos5, int[] pos6) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
        this.pos4 = pos4;
        this.pos5 = pos5;
        this.pos6 = pos6;
    }

    public GamePresenter() {
    }

    public void setPos(int[] pos1, int[] pos2, int[] pos3, int[] pos4,
                         int[] pos5, int[] pos6) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
        this.pos4 = pos4;
        this.pos5 = pos5;
        this.pos6 = pos6;
    }

    public List<MatchObjEntity> newGameByTopic(List<MatchObjEntity> listAll, int topic) {
        List<MatchObjEntity> list = new ArrayList<>();
        list = GameProvider.getInstance(DBHelper.getInstance(Global.mContext)).newGamebyId(listAll, topic);
        return list;
    }

    public void fillListVN(List<String> listChuCai, String[] names) {
        listChuCai.removeAll(listChuCai);
        for (int i = 0; i < names.length; i++) {
            listChuCai.add(names[i]);
        }
        listChuCai.remove(9);
        listChuCai.remove(12);
        listChuCai.remove(27);
        listChuCai.remove(29);
    }

    public void fillListEN(List<String> listChuCai, String[] names) {
        listChuCai.removeAll(listChuCai);
        for (int i = 0; i < names.length; i++) {
            listChuCai.add(names[i]);
        }
        listChuCai.remove(1);
        listChuCai.remove(1);
        listChuCai.remove(4);
        listChuCai.remove(5);
        listChuCai.remove(15);
        listChuCai.remove(15);
        listChuCai.remove(21);
    }

    public int checkLocate(float xLocate, float yLocate, float sizeWidth, float sizeHeight) {
        if (xLocate >= pos1[0] && xLocate <= (pos1[0] + sizeWidth)
                && yLocate >= pos1[1] && yLocate <= (pos1[1] + sizeHeight)) {
            return 1;
        } else if (xLocate >= pos2[0] && xLocate <= (pos2[0] + sizeWidth)
                && yLocate >= pos2[1] && yLocate <= (pos2[1] + sizeHeight)) {
            return 2;
        } else if (xLocate >= pos3[0] && xLocate <= (pos3[0] + sizeWidth)
                && yLocate >= pos3[1] && yLocate <= (pos3[1] + sizeHeight)) {
            return 3;
        } else if (xLocate >= pos4[0] && xLocate <= (pos4[0] + sizeWidth)
                && yLocate >= pos4[1] && yLocate <= (pos4[1] + sizeHeight)) {
            return 4;
        } else if (xLocate >= pos5[0] && xLocate <= (pos5[0] + sizeWidth)
                && yLocate >= pos5[1] && yLocate <= (pos5[1] + sizeHeight)) {
            return 5;
        } else if (xLocate >= pos6[0] && xLocate <= (pos6[0] + sizeWidth)
                && yLocate >= pos6[1] && yLocate <= (pos6[1] + sizeHeight)) {
            return 6;
        }
        return 0;
    }

    public PointDraw toaDoVe(int indexHinh, float sizeWidth, float sizeHeight) {
        PointDraw point = new PointDraw();
        switch (indexHinh) {
            case 1:
                point.setxLocate(pos1[0] + sizeWidth);
                point.setyLocate(pos1[1] + sizeHeight / 2);
                break;
            case 2:
                point.setxLocate(pos2[0]);
                point.setyLocate(pos2[1] + sizeHeight / 2);
                break;
            case 3:
                point.setxLocate(pos3[0] + sizeWidth);
                point.setyLocate((2 * pos3[1] + sizeHeight) / 2);
                break;
            case 4:
                point.setxLocate(pos4[0]);
                point.setyLocate((2 * pos4[1] + sizeHeight) / 2);
                break;
            case 5:
                point.setxLocate(pos5[0] + sizeWidth);
                point.setyLocate((2 * pos5[1] + sizeHeight) / 2);
                break;
            case 6:
                point.setxLocate(pos6[0]);
                point.setyLocate((2 * pos6[1] + sizeHeight) / 2);
                break;
            default:
                break;
        }
        return point;
    }

    public void getNameImage(ArrayList<String> nameArray, List<String> listChuCai) {
        int rd1 = getNumberRandom(listChuCai.size() - 1);
        int rd2 = getNumberRandom(listChuCai.size() - 1);
        int rd3 = getNumberRandom(listChuCai.size() - 1);
        while (rd1 == rd2) {
            rd2 = getNumberRandom(listChuCai.size() - 1);
        }
        while (rd1 == rd3 || rd2 == rd3) {
            rd3 = getNumberRandom(listChuCai.size() - 1);
        }
        nameArray.add(listChuCai.get(rd1));
        nameArray.add(listChuCai.get(rd2));
        nameArray.add(listChuCai.get(rd3));
    }

    public int imageRandom(int a) {
        int rd = getNumberRandom(a);
        if (rd >= 0 && rd <= 3) {
            return 0;
        } else if (rd >= 4 && rd <= 6) {
            return 1;
        } else {
            return 2;
        }
    }

    public int getNumberRandom(int number) {
        if (number < 0) {
            return 0;
        }
        Random random = new Random();
        int n = random.nextInt(number);
        return n;
    }

    public List<MatchObjEntity> getAllListObj(int topic) {
        /*Toan bo danh sach theo chu de*/
        listObjAll = new ArrayList<>();
        if (topic == 4) {
            listObjAll = DBHelper.getInstance(Global.mContext).getListGameAllById(topic - 1);
        } else {
            listObjAll = DBHelper.getInstance(Global.mContext).getListGameAllById(topic);
        }
        return listObjAll;
    }
}
