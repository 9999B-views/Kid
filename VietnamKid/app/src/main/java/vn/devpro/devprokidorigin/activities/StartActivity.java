package vn.devpro.devprokidorigin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;

import vn.devpro.devprokidorigin.R;
import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.interfaces.UpdaterListener;
import vn.devpro.devprokidorigin.utils.AdMob.RewardedVideo;
import vn.devpro.devprokidorigin.utils.FileProcessor;
import vn.devpro.devprokidorigin.utils.Global;
import vn.devpro.devprokidorigin.utils.MyException;
import vn.devpro.devprokidorigin.utils.NetworkReceiver;
import vn.devpro.devprokidorigin.utils.ResourceVersion;
import vn.devpro.devprokidorigin.utils.UpdateTask;
import vn.devpro.devprokidorigin.utils.Utils;

public class StartActivity extends AppCompatActivity implements UpdaterListener {

    private ProgressBar progressBar;
    private ResourceVersion resourceVersion;
    public static RewardedVideo rewardedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        addControls();
        addEvents();
    }

    private void addEvents() {
        // Khởi tạo biến cục bộ
        Global.initGlobal(getApplicationContext());

        resourceVersion = new ResourceVersion(this);
        resourceVersion.getResourceVersion(); // đã được lưu vào Global.resourceVersion

        if (NetworkReceiver.isConnected()) {
            // kiểm tra và cập nhật dữ liệu
            UpdateTask updateTask = new UpdateTask(this, progressBar, true);
            updateTask.setUpdaterListener(this);
            updateTask.execute();
        } else {
            // tạo tài nguyên demo
            createAppDemo();
        }
    }

    private void addControls() {
        progressBar = findViewById(R.id.pgbUpdate);
        rewardedVideo = RewardedVideo.loadRewardedVideoAds(this);
    }

    @Override
    public void updateOK() {
        goToMainactivity();
    }

    @Override
    public void updateFail(boolean isFirebase) {
        if (isFirebase) {
            Log.d("update firebase", "fail -> chuyển máy chủ cập nhật");
            Utils.showToast(this, "Chuyển máy chủ cập nhật!");
            UpdateTask updateTask = new UpdateTask(this, progressBar, false);
            updateTask.setUpdaterListener(this);
            updateTask.execute();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Cập nhật bị gián đoạn!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // tạo app demo
                            createAppDemo();
                            // chuyển đến màn hình chính: mainActivity
                            goToMainactivity();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    private void createAppDemo() {
        if (Global.resourceVersion.equals(Global.NO_DATA_KEY)) {
            try {

                boolean copyDatabase = FileProcessor.copyFileFromAsset(this, Global.databaseFileName, Global.pathSysDatabase.getPath());
                if(!copyDatabase){
                    throw new MyException("copy demo database lỗi");
                }
                Log.d("copyDemoDataBase", "true");

                boolean copyDemoFile = FileProcessor.copyFileFromAsset(this, Global.dataDemoFileName, Global.pathDataDemoFile);
                if (!copyDemoFile) {
                    throw new MyException("copy demo resource lỗi");
                }
                Log.d("copyDemoFile", copyDemoFile + "");

                boolean unzipDemoFile = FileProcessor.unzipFile(Global.pathDataDemoFile, Global.pathFiles.getPath() + "/");
                if (!unzipDemoFile) {
                    throw new MyException("giải nén demo resource lỗi");
                }
                Log.d("unzipDemoFile", unzipDemoFile + "");

                boolean updateResource = resourceVersion.setResourceVersion(Global.DEMO_DATA_KEY);
                if (!updateResource) {
                    throw new MyException("cập nhật ResourceVersion lỗi");
                }
                Log.d("updateResource", updateResource + "");

                // chuyển đến màn hình chính: mainActivity
                goToMainactivity();
            } catch (MyException e) {
                Log.d("TaoDataDemo", e.getMessage());
            }
        } else {
            // app đã tải về tải nguyên
            // có thể phiên bản cũ
            // hoặc tài nguyên demo
            goToMainactivity();
        }
    }

    private void goToMainactivity() {
        // chuyển đến màn hình chính: mainActivity
        Intent toMain = new Intent(this, MainActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
    }
}

