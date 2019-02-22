package vn.devpro.devprokidorigin.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.models.entity.TopicName;
import vn.devpro.devprokidorigin.models.entity.game.FindShadow;
import vn.devpro.devprokidorigin.models.entity.game.MatchObjEntity;
import vn.devpro.devprokidorigin.models.entity.hocchucai.Question;
import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by Laptop88 on 3/17/2018.
 */

public class DBHelper extends SQLiteDataController {
    private final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper dbHelper;
    private final String TABLE_TOPIC_ITEM = "topic_item";
    private final String TABLE_TOPIC_NAME = "topic_name";
    private final String COLUMN_SHOW_RATIO = "show_ratio";
    private final String COLUMN_CORRECT_COUNT = "correct_count";
    private final String COLUMN_LOCK = "lock";


    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            return dbHelper;
        }
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context);
    }

    @Nullable
    public ArrayList<TopicName> getTopicName() {
        ArrayList<TopicName> listTopic = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_NAME;
        Cursor cs;
        try {
            openDataBase();
            if (database == null) return listTopic;
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img;
                title_vn = cs.getString(1);
                title_en = cs.getString(2);
                img = cs.getString(3);
                int percent = cs.getInt(4);
                int lock = cs.getInt(5);

                TopicName topicName = new TopicName(id, title_vn, title_en, img, percent, lock);
                listTopic.add(topicName);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listTopic.size = 0");
            e.printStackTrace();
        }finally {
            if (database != null && database.isOpen())
                close();
        }

        return listTopic;
    }

    public ArrayList<TopicItem> getImageGame() {
        ArrayList<TopicItem> listImageGame = new ArrayList<>();
        String sql = "select * from topic_item where topic_id = 2 or topic_id = 4 or topic_id =5 or topic_id = 6 or topic_id = 8  or topic_id =3";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "Warning: database null --> listImageGame.size = 0");
                return listImageGame;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img_name, sound_vn, sound_en;

                int show_ratio, correct_count;
                title_vn = cs.getString(2);
                title_en = cs.getString(3);
                img_name = cs.getString(4);
                sound_vn = cs.getString(5);
                sound_en = cs.getString(6);
                show_ratio = cs.getInt(7);
                correct_count = cs.getInt(8);

                TopicItem item = new TopicItem(id, title_vn, title_en,
                        img_name, sound_vn, sound_en, show_ratio, correct_count);
                listImageGame.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listImageGame.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return listImageGame;
    }

    String[] imgs_vn = {"a_up", "aw_up", "aa_up", "b_up", "c_up", "d_up", "dd_up", "e_up", "ee_up", "g_up", "h_up", "i_up", "k_up", "l_up", "m_up", "n_up", "o_up", "oo_up", "ow_up", "p_up", "q_up", "r_up", "s_up", "t_up", "u_up", "uw_up", "v_up", "x_up", "y_up"};
    String[] imgs_en = {"a_up", "b_up", "c_up", "d_up", "e_up", "f_up", "g_up", "h_up", "i_up", "j_up", "k_up", "l_up", "m_up", "n_up", "o_up", "p_up", "q_up", "r_up", "s_up", "t_up", "u_up", "v_up", "w_up", "x_up", "y_up", "z_up"};

    public ArrayList<TopicItem> getTopicByid(int i) {
        ArrayList<TopicItem> listItem = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_ITEM + " where topic_id = '" + i + "'";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database is null --> listItem.size = 0");
                return listItem;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img_name, sound_vn, sound_en;

                int show_ratio, correct_count;
                title_vn = cs.getString(2);
                title_en = cs.getString(3);
                img_name = cs.getString(4);
                sound_vn = cs.getString(5);
                sound_en = cs.getString(6);
                show_ratio = cs.getInt(7);
                correct_count = cs.getInt(8);

                TopicItem item = new TopicItem(id, title_vn, title_en,
                        img_name, sound_vn, sound_en, show_ratio, correct_count);
                listItem.add(item);
            }
            if (i == 1) {
                standardizeImgName(listItem, listItem.size());
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listItem.size = 0");
           // e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return listItem;
    }

    private void standardizeImgName(ArrayList<TopicItem> pListImageForGame, int size) {
        int lang = Global.getLanguage();

        if (lang == Global.VN) {
            size = 29;
        } else {
            size = 26;
        }
        for (int i = 0; i < size; i++) {
            if (lang == Global.VN) {
                pListImageForGame.get(i).setImg_name(imgs_vn[i]);
            } else {
                pListImageForGame.get(i).setImg_name(imgs_en[i]);
            }
        }
    }

    @Nullable
    public String getSoundById(int id) {
        String sound = "";
        String sql = "select * from " + TABLE_TOPIC_ITEM + " where id = " + id;
        Cursor cs;
        Log.d("topicitem", id + "");
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database is null --> sound = null");
                return null;
            }
            cs = database.rawQuery(sql, null);
            cs.moveToFirst();
            if (Global.getLanguage() == Global.VN) {
                sound = cs.getString(5);
            } else {
                sound = cs.getString(6);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> soundName empty");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return sound;
    }

    @Nullable
    public String getSoundQuestion(int topic_id) {
        String soundQuestion = "";
        String sql = "select * from question where topic_id = '" + topic_id + "'";
        Log.d(TAG, "Cau truy van = " + sql);
        Cursor cs = null;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> soundQuestion = null");
                return null;
            }
            cs = database.rawQuery(sql, null);
            if (cs == null) {
                Log.d(TAG, "Cs null");
            }
            while (cs.moveToNext()) {
                if (cs.getInt(6) == 1) {
                    if (Global.getLanguage() == Global.VN) {
                        soundQuestion = cs.getString(3);
                        Log.d(TAG, "Am = " + soundQuestion);
                    } else {
                        soundQuestion = cs.getString(5);
                        Log.d(TAG, "Am = " + soundQuestion);
                    }
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> soundQuestion empty");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return soundQuestion;
    }

    public ArrayList<TopicItem> getChuCai() {
        ArrayList<TopicItem> listChuCai = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_ITEM + " where topic_id = 1";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listChuCai.size = 0");
                return listChuCai;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img_name, sound_vn, sound_en;
                int show_ratio, correct_count;
                title_vn = cs.getString(2);
                title_en = cs.getString(3);
                sound_vn = cs.getString(5);
                sound_en = cs.getString(6);
                show_ratio = cs.getInt(7);
                correct_count = cs.getInt(8);
                TopicItem item = new TopicItem(id, title_vn, title_en, null, sound_vn, sound_en, show_ratio, correct_count);
                listChuCai.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listChuCai.size = 0");
            e.printStackTrace();
        }finally {
            if (database != null && database.isOpen())
                close();
        }
        return listChuCai;
    }

    public int updateColumnShowRatio(int id, int params) {
        int nums = -1;
        int i = 0;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant update Column show ratio");
                return -1;
            }
            database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOW_RATIO, params);

            final String where = "id = ?";
            nums = database.update(TABLE_TOPIC_ITEM, values, where, new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return nums;
    }

    public int resetShowRatio_Correct() {
        /*Cap nhat lai toan bo khi thay doi ngon ngu*/
        int nums = -1;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant reset Column show ratio");
                return -1;
            }
            database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOW_RATIO, 0);
            values.put(COLUMN_CORRECT_COUNT, 0);
            final String where = "topic_id = ?";
            nums = database.update(TABLE_TOPIC_ITEM, values, where, new String[]{String.valueOf(1)});
        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return nums;
    }

    public int updateAllColumnShowRatio() {
        /*Cap nhat lai toan bo khi thay doi ngon ngu*/
        int nums = -1;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant update All Column show ratio");
                return -1;
            }
            database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SHOW_RATIO, 0);
            final String where = "topic_id = ?";
            nums = database.update(TABLE_TOPIC_ITEM, values, where, new String[]{String.valueOf(1)});
        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return nums;
    }

    public int checkColumnShowRatio(int id) {
        /*Kiem tra xem chu nay da dc hoc chua*/
        int check = -1;
        String SQL = "select * from " + TABLE_TOPIC_ITEM + " where id = '" + id + "'";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant check Column show ratio");
                return -1;
            }
            cs = database.rawQuery(SQL, null);
            if (cs.moveToFirst()) {
                check = cs.getInt(7);
            }
        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> reuturn -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return check;
    }

    public List<Question> getAllQuestion(int topic_id) {
        List<Question> listQuestion = new ArrayList<>();
        String SQL = "SELECT * from " + TABLE_TOPIC_ITEM + " where topic_id = '" + topic_id + "'";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listQuestion.size = 0");
                return listQuestion;
            }
            cs = database.rawQuery(SQL, null);
            while (cs.moveToNext()) {
                Question question = new Question(cs.getInt(0), cs.getString(2), cs.getString(3),
                        cs.getString(5), cs.getString(6),
                        cs.getInt(7), cs.getInt(8));
                listQuestion.add(question);
            }

        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listQuestion.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return listQuestion;
    }

    public ArrayList<TopicName> listTopic() {
        ArrayList<TopicName> listTopic = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_NAME;
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listTopic.size = 0");
                return listTopic;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img;
                int percent, lock;
                title_vn = cs.getString(1);
                title_en = cs.getString(2);
                img = cs.getString(3);
                percent = cs.getInt(4);
                lock = cs.getInt(5);
                TopicName item = new TopicName(id, title_vn, title_en, img, percent, lock);
                listTopic.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, "cant open db --> listTopic.size = 0");
            //e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return listTopic;
    }

    public List<Question> getQuestion(int topic_id, int show_ratio, int correct_count) {
        List<Question> questionList = new ArrayList<>();
        String SQL = "SELECT * from " + TABLE_TOPIC_ITEM + " where topic_id = '" + topic_id + "'" +
                " AND show_ratio = '" + show_ratio + "' AND correct_count = '" + correct_count + "'";
        Cursor cs;

        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> questionList.size = 0");
                return questionList;
            }
            cs = database.rawQuery(SQL, null);
            while (cs.moveToNext()) {
                //TODO; Khong chay vao ham nay?
                Question question = new Question(cs.getInt(0), cs.getString(2), cs.getString(3),
                        cs.getString(5), cs.getString(6),
                        cs.getInt(7), cs.getInt(8));
                questionList.add(question);
            }

        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> questionList.size = 0");
            //e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return questionList;
    }


    public int resetColumnCorrectCount() {
        /*Cap nhat lai CorrectCount khi chuyen ngon ngu*/
        int check = -1;
        try {
            openDataBase();
            database = this.getWritableDatabase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant perform action");
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(COLUMN_CORRECT_COUNT, 0);
            final String where = "topic_id = ?";
            check = database.update(TABLE_TOPIC_ITEM, values, where, new String[]{String.valueOf(1)});
        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
//            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return check;
    }

    public int updateColumnCorrectCount(int id, int numb) {
        int nums = -1;
        try {
            openDataBase();
            database = this.getWritableDatabase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant perform action");
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(COLUMN_CORRECT_COUNT, numb);

            final String where = "id = ?";
            nums = database.update(TABLE_TOPIC_ITEM, values, where, new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return nums;
    }

    public int updateColumnLock(int id) {
        int nums = -1;
        try {
            openDataBase();
            database = this.getWritableDatabase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant perform action");
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOCK, 0);

            final String where = "id = ?";
            nums = database.update(TABLE_TOPIC_NAME, values, where, new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return nums;
    }

    public int updateColumnLock() {
        int nums = -1;
        try {
            openDataBase();
            database = this.getWritableDatabase();
            if (database == null) {
                Log.e(TAG, "database = null --> cant perform action");
                return -1;
            }
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOCK, 0);

            nums = database.update(TABLE_TOPIC_NAME, values, null, null);
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> return -1");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return nums;
    }

    public List<MatchObjEntity> getListGameAllById(int topic) {
        ArrayList<MatchObjEntity> list = new ArrayList<>();
        String SQL = "SELECT * from match_obj where chu_de = '" + topic + "'";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> list.size = 0");
                return list;
            }
            cs = database.rawQuery(SQL, null);
            while (cs.moveToNext()) {
                /*Thu de cai nay len dau*/
                MatchObjEntity matchObjEntity = new MatchObjEntity(cs.getInt(0),
                        cs.getString(1), cs.getInt(2), cs.getInt(3),
                        cs.getString(5), cs.getString(6));
                list.add(matchObjEntity);
            }
        } catch (Exception e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> list.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
                close();
        }
        return list;
    }

    public ArrayList<TopicItem> getTopicItemForUpdater() {
        ArrayList<TopicItem> listItem = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_ITEM;
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listItem.size = 0");
                return listItem;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String title_vn, title_en, img_name, sound_vn, sound_en;
                int show_ratio, correct_count;
                title_vn = cs.getString(2);
                title_en = cs.getString(3);
                img_name = cs.getString(4);
                sound_vn = cs.getString(5);
                sound_en = cs.getString(6);
                show_ratio = cs.getInt(7);
                correct_count = cs.getInt(8);

                TopicItem item = new TopicItem(id, title_vn, title_en,
                        img_name, sound_vn, sound_en, show_ratio, correct_count);
                listItem.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listItem.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return listItem;
    }

    public ArrayList<TopicName> getTopicNameForUpdater() {
        ArrayList<TopicName> listItem = new ArrayList<>();
        String sql = "select * from " + TABLE_TOPIC_NAME;
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listItem.size = 0");
                return listItem;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id, percent;
                String title_vn, title_en, img;

                id = cs.getInt(0);
                title_vn = cs.getString(1);
                title_en = cs.getString(2);
                img = cs.getString(3);
                percent = cs.getInt(4);
                int lock = cs.getInt(5);
                TopicName item = new TopicName(id, title_vn, title_en, img, percent, lock);
                listItem.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listItem.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return listItem;
    }

    public void updateTopicItem(int idItem, int show_ratio, int correct_count) {
        database = this.getWritableDatabase();
        if (database == null) {
            Log.e(TAG, "updateTopicItem: database = null --> cant perform action");
            return;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOW_RATIO, show_ratio);
        values.put(COLUMN_CORRECT_COUNT, correct_count);
        database.update(TABLE_TOPIC_ITEM, values, "id =" + idItem, null);
        database.close();
    }

    public void updateTopicName(int idTopic, int percent, int lock) {
        database = this.getWritableDatabase();
        if (database == null) {
            Log.e(TAG, "updateTopicName: database = null --> cant perform action");
            return;
        }
        ContentValues values = new ContentValues();
        values.put("percent", percent);
        values.put("lock", lock);
        database.update(TABLE_TOPIC_NAME, values, "id=" + idTopic, null);
        database.close();
    }

    public ArrayList<FindShadow> getImageGameFS(int i) {
        ArrayList<FindShadow> list = new ArrayList<>();
        String sql = "select * from find_shadow where topic_id ='" + i + "'";
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "database = null --> listItem.size = 0");
                return list;
            }
            cs = database.rawQuery(sql, null);
            while (cs.moveToNext()) {
                int id = cs.getInt(0);
                String origin_img, shadow_img, sound_vn, sound_en;
                origin_img = cs.getString(2);
                shadow_img = cs.getString(3);
                sound_vn = cs.getString(4);
                sound_en = cs.getString(5);

                FindShadow item = new FindShadow(id, origin_img, shadow_img,
                        sound_vn, sound_en);
                list.add(item);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> listItem.size = 0");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return list;
    }

    @Nullable
    public String soundForDiaLogHTK(int i) {
        String s = null;
        String sql;
        String sql2 = "select * from find_shadow where topic_id ='" + i + "'";
        if (Global.getLanguage() == Global.VN) {
            sql = "select sound_vn from question where is_main = '1' AND topic_id ='" + i + "'";
        } else {
            sql = "select sound_en from question where is_main = '1' AND topic_id ='" + i + "'";
        }
        Cursor cs;
        try {
            openDataBase();
            if (database == null) {
                Log.e(TAG, "soundForDiaLogHTK: database = null --> soud name return null");
                return null;
            }
            cs = database.rawQuery(sql, null);
            if (cs.getCount() > 0) {
                cs.moveToFirst();
                s = cs.getString(0);
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> soud name return null");
            e.printStackTrace();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return s;
    }

    public void updateShowratio(ArrayList<TopicItem> listItem) {
        int i = 0;

        try {
            openDataBase();
            if (database == null) {
                Log.e("DATABASE:", "database null, cant update show ratio");
                return;
            }
            for (TopicItem item : listItem) {
                ContentValues values = new ContentValues();
                String numb = String.valueOf(item.getShow_ratio());
                values.put("show_ratio", numb);
                database.update("topic_item", values, "id = ?", new String[]{String.valueOf(item.getId())});
            }
        } catch (SQLException e) {
            Log.e(TAG, "--->" + e.getMessage());
            Log.e(TAG, "cant open db --> cant update show ratio");
            e.getMessage();
        } finally {
            if (database != null && database.isOpen())
            close();
        }
    }

    public float updatePercent(int topicID) {
        int i = 0;
        float number = 0;
        ArrayList<TopicItem> list = new ArrayList<>();
        list = getTopicByid(topicID);
        if (list == null) return number;
        for (TopicItem item : list) {
            i = item.getShow_ratio() + i;
        }
        try {
            openDataBase();
            if (database == null) throw new SQLException("database null");
            ContentValues values = new ContentValues();
            number = (float) i / (list.size() * 3);
            values.put("percent", String.valueOf(number));
            database.update("topic_name", values, "id = ?", new String[]{String.valueOf(topicID)});
        } catch (SQLException e) {
            Log.e("Err", "updatePercent  error " + e.getMessage());
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return number;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public Integer getTopicCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TOPIC_NAME;
        try {
            openDataBase();
            if (database == null) throw new SQLException("database null");
            Cursor cursor = database.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;
        } catch (Exception e) {
            Log.e("Err", "getTopicCount  error:" + e.getMessage());
        } finally {
            if (database != null && database.isOpen())
            close();
        }
        return 0;
    }


}
