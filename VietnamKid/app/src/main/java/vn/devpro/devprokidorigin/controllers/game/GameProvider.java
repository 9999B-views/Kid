package vn.devpro.devprokidorigin.controllers.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.models.entity.game.MatchObjEntity;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by hoang-ubuntu on 03/05/2018.
 */

public class GameProvider {
    private final String TAG = GameProvider.class.getSimpleName();
    private DBHelper dbHelper;
    private static GameProvider instance;
    private List<MatchObjEntity> listThreeObj;

    public GameProvider(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public GameProvider() {
    }

    public static GameProvider getInstance(DBHelper dbHelper) {
        if (instance == null) {
            instance = new GameProvider(dbHelper);
        }
        return instance;
    }

    public List<MatchObjEntity> newGamebyId(List<MatchObjEntity> listObjAll, int topic) {
        listThreeObj = new ArrayList<>();
        if (listObjAll == null) {
            return listThreeObj;
        } else {
            /*3 Obj random*/
            listThreeObj = getThreeIdGame(listObjAll, topic);
            // Lay 3 doi tuong con lai
            MatchObjEntity matchObjEntity = new MatchObjEntity();
            if (topic == 3) {
                /*Loc ra cac doi tuong co cung truong Count*/
                addObj(listObjAll, listThreeObj.get(0).getCount(), listThreeObj.get(0).getId());
                addObj(listObjAll, listThreeObj.get(1).getCount(), listThreeObj.get(1).getId());
                addObj(listObjAll, listThreeObj.get(2).getCount(), listThreeObj.get(2).getId());
            } else if (topic == 4 || topic == 6 || topic == 7) {
            } else {
                /*Lay tiep 3 hinh con lai dua vao thuoc tinh pair*/
                matchObjEntity = getMatchObj(listThreeObj.get(0).getPair(), listObjAll);
                listThreeObj.add(matchObjEntity);
                matchObjEntity = getMatchObj(listThreeObj.get(1).getPair(), listObjAll);
                listThreeObj.add(matchObjEntity);
                matchObjEntity = getMatchObj(listThreeObj.get(2).getPair(), listObjAll);
                listThreeObj.add(matchObjEntity);
            }
        }
        return listThreeObj;
    }

    private void addObj(List<MatchObjEntity> listObjAll, int count, int id) {
        List<MatchObjEntity> listFill = new ArrayList<>();
        listFill = fillListByCount(listObjAll, count, id);
        int random = getNumberRandom(listFill.size() - 1);
        listThreeObj.add(listFill.get(random));
    }

    private List<MatchObjEntity> getThreeIdGame(List<MatchObjEntity> listObjAll, int topic) {
        /*Lay danh sach 3 cau hoi*/
        List<MatchObjEntity> listObj = new ArrayList<>();
        int rd1 = getNumberRandom(listObjAll.size() - 1);
        int rd2 = getNumberRandom(listObjAll.size() - 1);
        int rd3 = -1;
        /*So lay phai khac nhau - va khong lien quan den nhau*/
        switch (topic){
            case 1:
                while (rd1 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd2).getPair()) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd3).getPair() ||
                        listObjAll.get(rd2).getId() == listObjAll.get(rd3).getPair()) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 2:
                while (rd1 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd2).getPair()) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd3).getPair() ||
                        listObjAll.get(rd2).getId() == listObjAll.get(rd3).getPair()) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 3:
                while (rd1 == rd2 || listObjAll.get(rd1).getCount() == listObjAll.get(rd2).getCount()) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd3).getCount() == listObjAll.get(rd1).getCount()
                        || listObjAll.get(rd3).getCount() == listObjAll.get(rd2).getCount()) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 4:
                while (rd1 == rd2 || listObjAll.get(rd1).getCount() == listObjAll.get(rd2).getCount()) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd3).getCount() == listObjAll.get(rd1).getCount()
                        || listObjAll.get(rd3).getCount() == listObjAll.get(rd2).getCount()) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 5:
                while (rd1 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd2).getPair()) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd1).getId() == listObjAll.get(rd3).getPair() ||
                        listObjAll.get(rd2).getId() == listObjAll.get(rd3).getPair()) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 6:
                while (rd1 == rd2) {
                    rd2 = getNumberRandom(listObjAll.size() - 1);
                }
                rd3 = getNumberRandom(listObjAll.size() - 1);
                while (rd3 == rd1 || rd3 == rd2) {
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                }
                break;
            case 7:
                if (Global.getLanguage() == Global.VN) {
                    while (rd1 == rd2 || listObjAll.get(rd1).getLetter_vn().equals(listObjAll.get(rd2).getLetter_vn())) {
                        rd2 = getNumberRandom(listObjAll.size() - 1);
                    }
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                    while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd3).getLetter_vn().equals(listObjAll.get(rd1).getLetter_vn())
                            || listObjAll.get(rd3).getLetter_vn().equals(listObjAll.get(rd2).getLetter_vn())) {
                        rd3 = getNumberRandom(listObjAll.size() - 1);
                    }
                } else {
                    while (rd1 == rd2 || listObjAll.get(rd1).getLetter_vn().equals(listObjAll.get(rd2).getLetter_vn())) {
                        rd2 = getNumberRandom(listObjAll.size() - 1);
                    }
                    rd3 = getNumberRandom(listObjAll.size() - 1);
                    while (rd3 == rd1 || rd3 == rd2 || listObjAll.get(rd3).getLetter_en().equals(listObjAll.get(rd1).getLetter_en())
                            || listObjAll.get(rd3).getLetter_en().equals(listObjAll.get(rd2).getLetter_en())) {
                        rd3 = getNumberRandom(listObjAll.size() - 1);
                    }
                }
                break;
            default:
                break;
        }

        MatchObjEntity matchObjEntity = new MatchObjEntity();
        matchObjEntity = getMatchObj(listObjAll.get(rd1).getId(), listObjAll);
        listObj.add(matchObjEntity);
        matchObjEntity = getMatchObj(listObjAll.get(rd2).getId(), listObjAll);
        listObj.add(matchObjEntity);
        matchObjEntity = getMatchObj(listObjAll.get(rd3).getId(), listObjAll);
        listObj.add(matchObjEntity);
        return listObj;
    }

    private MatchObjEntity getMatchObj(int id, List<MatchObjEntity> listObjAll) {
        int index = -1;
        for (int i = 0; i < listObjAll.size(); i++) {
            if (listObjAll.get(i).getId() == id) {
                index = i;
                break;
            }
        }
        return listObjAll.get(index);
    }

    private List<MatchObjEntity> fillListByCount(List<MatchObjEntity> listObjAll, int count, int id) {
        List<MatchObjEntity> listFill = new ArrayList<>();
        for (int i = 0; i < listObjAll.size(); i++) {
            if (listObjAll.get(i).getCount() == count && listObjAll.get(i).getId() != id) {
                listFill.add(listObjAll.get(i));
            }
        }
        return listFill;
    }

    private int getNumberRandom(int number) {
        if (number < 0) {
            return 0;
        }
        Random random = new Random();
        int n = random.nextInt(number);
        return n;
    }
}
