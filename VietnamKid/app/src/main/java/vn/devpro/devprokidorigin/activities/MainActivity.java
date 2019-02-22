
package vn.devpro.devprokidorigin.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.interfaces.MainClick;
import vn.devpro.devprokidorigin.interfaces.MainFuctionClick;
import vn.devpro.devprokidorigin.utils.FileProcessor;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private ImageButton ibtnQuangCao, ibtnNgonNgu, ibtnCaiDat, ibtnHocTap, ibtnTroChoi;
    int total_Topic = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // chương trình chính
        addControls();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.updateButtonLanguage(ibtnNgonNgu);
        new GetTotalTopic().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addControls() {
        // thêm control imagebutton chuc nang
        ibtnQuangCao = findViewById(R.id.ibtnQuangCao);
        ibtnNgonNgu = findViewById(R.id.ibtnNgonNgu);
        ibtnCaiDat = findViewById(R.id.ibtnCaiDat);

        //update for language btn
        Global.updateButtonLanguage(ibtnNgonNgu);
        /*  xoa file cache */
        Utils.clearMp3FileCache(getApplicationContext());

        // thêm control imagebutton chinh
        ibtnHocTap = findViewById(R.id.ibtnHocTap);
        ibtnTroChoi = findViewById(R.id.ibtnTroChoi);

        // tam thoi an more app
        ibtnQuangCao.setVisibility(View.GONE);

        // xử lý trường hợp không có db
        Utils.fixNullDatabase(getApplicationContext());
    }

    private void addEvents() {
        // sự kiện click chức năng
        ibtnQuangCao.setOnClickListener(new MainFuctionClick(this));
        ibtnNgonNgu.setOnClickListener(new MainFuctionClick(this));
        ibtnCaiDat.setOnClickListener(new MainFuctionClick(this));

        // sự kiện click chính
        boolean checkDb = DBHelper.getInstance(this).checkExistDataBase();
        if (!checkDb) {
            boolean copyDemoFile = FileProcessor.copyFileFromAsset(this, "devkid.db", Global.pathSysDatabase.getPath());
            Log.i("TAG", "copyDemoFile" + copyDemoFile);
            if (!copyDemoFile) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setMessage("Xin lỗi App đã xảy ra lỗi trong quá trình cài đặt. Vui lòng cài lại Ứng dụng!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.exit(0);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
        ibtnHocTap.setOnClickListener(new MainClick(Global.mContext).putExtra(total_Topic));
        ibtnTroChoi.setOnClickListener(new MainClick(Global.mContext));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class GetTotalTopic extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... pVoids) {
            Integer count = DBHelper.getInstance(Global.mContext).getTopicCount();
            return count;
        }

        @Override
        protected void onPostExecute(Integer pInteger) {
            total_Topic = pInteger;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}

