package vn.devpro.devprokidorigin.databases;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import vn.devpro.devprokidorigin.utils.Global;

/**
 * Created by Laptop88 on 3/17/2018.
 */

public class SQLiteDataController extends SQLiteOpenHelper {
    private final String TAG = SQLiteDataController.class.getSimpleName();

    protected SQLiteDatabase database;
    private final Context mContext;

    public SQLiteDataController(Context con) {
        super(con, Global.databaseFileName, null, 1);
        this.mContext = con;
    }

    /**
     * copy database from assets to the device if not existed
     *
     * @return true if not exist and create database success
     */
    public boolean isCreatedDatabase() {
        // Default là đã có DB
        boolean result = true;
        // Nếu chưa tồn tại DB thì copy từ Assets vào Data
        if (!checkExistDataBase()) {
            this.getReadableDatabase();
            try {
                copyDataBase();
                result = false;
            } catch (Exception e) {
                throw new Error("Error copying database");
            }
        }

        return result;
    }

    /**
     * check whether database exist on the device?
     *
     * @return true if existed
     */
    public boolean checkExistDataBase() {
        try {
            File fileDB = Global.pathSysDatabase;
            if (fileDB.exists()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * copy database from assets folder to the device
     *
     * @throws IOException
     */

    public boolean copyDataBase() {
        try {
            InputStream myInput = mContext.getAssets().open(Global.databaseFileName);
            OutputStream myOutput = new FileOutputStream(Global.pathSysDatabase);

            File f = new File(Global.pathSysDatabase.getParent());
            if (!f.exists())
                f.mkdir();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            return true;
        } catch (
                IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * delete database file
     *
     * @return
     */
    public boolean deleteDatabase() {
        return Global.pathSysDatabase.delete();
    }

    /**
     * open database
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        if (!checkExistDataBase()){
            throw new SQLException("file data khong ton tai");
        }else {
                database = SQLiteDatabase.openDatabase(Global.pathSysDatabase.getPath(), null,
                        SQLiteDatabase.OPEN_READWRITE);
                //Log.d("dbName", Global.pathSysDatabase.getPath());
        }
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // do nothing
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public int deleteData_From_Table(String tbName) {

        int result = 0;
        try {
            openDataBase();
            database.beginTransaction();
            result = database.delete(tbName, null, null);
            if (result >= 0) {
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            database.endTransaction();
            close();
        } finally {
            database.endTransaction();
            close();
        }

        return result;
    }

}