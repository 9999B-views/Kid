package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.devpro.devprokidorigin.databases.DBHelper;
import vn.devpro.devprokidorigin.interfaces.DownloadingListener;
import vn.devpro.devprokidorigin.interfaces.UpdaterListener;
import vn.devpro.devprokidorigin.models.App;
import vn.devpro.devprokidorigin.models.entity.TopicItem;
import vn.devpro.devprokidorigin.models.entity.TopicName;

/**
 * Created by NamQuoc on 3/29/2018.
 */

public class UpdateTask extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private ProgressBar progressBar;

    private String versionClient;
    private ArrayList<String> listVersionServer;
    private int indexVersion;
    private int countVersion;
    public static int B = 50;
    private int perVersion;
    private UpdaterListener updaterListener;
    private boolean isFirebase;
    private boolean downloadFirebaseFail = false;

    public UpdateTask(Context context, ProgressBar progressBar, boolean isFirebase) {
        this.context = context;
        this.progressBar = progressBar;
        this.isFirebase = isFirebase;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //  1.  Tải file versionServer.txt

        DownloadingListener downloadVersionFileListener = new DownloadingListener() {

            @Override
            public void downloadProgress(Double percent) {

            }

            @Override
            public void downloadOK(File file) {
                Log.i("versionServer", file.getPath() + " Tải về thành công");
                nextStep(file);
            }

            @Override
            public void downloadFail() {
                Log.e("versionServer", "Tải về lỗi");
                // callback: thông báo update không thành công
                if (isFirebase) {
                    downloadFirebaseFail = true;
                }
                endUpdate(false);
            }
        };

        if (isFirebase) {
            FirebaseProcessor downloadVersionServer = new FirebaseProcessor();
            downloadVersionServer.downloadFile(Global.pathFiles.getPath() + "/", Global.versionSeverFileName);
            downloadVersionServer.setListener(downloadVersionFileListener);
        } else {
            DownloadFileFromServer myDownload1 = new DownloadFileFromServer();
            myDownload1.setDownloadingListener(downloadVersionFileListener);
            myDownload1.downloadFile(Global.urlServer, Global.pathFiles.getPath() + "/", Global.versionSeverFileName);
        }

        return null;
    }


    private void nextStep(File pathFileVersionServer) {
        // get versionClient
        switch (Global.resourceVersion) {
            case Global.NO_DATA_KEY:
                versionClient = "0"; // chưa có data, tải tất cả file trên firebase
                break;
            case Global.DEMO_DATA_KEY:
                versionClient = "0"; // đã có data demo, tải tất cả file trên firebase
                break;
            default:
                if (Global.resourceVersion != null) {
                    versionClient = Global.resourceVersion;
                }
                break;
        }
        // end

        // đọc file versionServer.txt
        listVersionServer = new ArrayList<String>();
        listVersionServer = FileProcessor.readFileText(pathFileVersionServer);

        for (String ver : listVersionServer) {
            Log.i("versionServers", ver);
        }
        // end

        // Check phiên bản trên client và server
        if (!listVersionServer.isEmpty()) {
            // lấy vị trí phiên bản hiện tại của client trên server
            indexVersion = listVersionServer.indexOf(versionClient);
            countVersion = listVersionServer.size();

            //  khởi tạo topicItems, topicNames dùng cho backup và restore dữ liệu

            perVersion = 100 / countVersion;
            downloadFile(indexVersion + 1, isFirebase);
        } else {
            Log.e("versionServers", "empty");
        }
        // end
    }

    private void downloadFile(final int i, final boolean isFirebase) {
        if (i < listVersionServer.size()) {

            DownloadingListener downloadingListener = new DownloadingListener() {
                @Override
                public void downloadProgress(Double percent) {
                    Integer per = (int) Math.round(perVersion * i + percent * perVersion);
                    updateProgress(per);
                    Log.i("download", listVersionServer.get(i) + " - " + per);
                }

                @Override
                public void downloadOK(File file) {
                    execAfterDownloadFile(file, isFirebase);
                }

                @Override
                public void downloadFail() {
                    if (isFirebase) {
                        downloadFirebaseFail = true;
                    }
                    endUpdate(false);
                }
            };

            if (isFirebase) {
                FirebaseProcessor myFirebase = new FirebaseProcessor();
                myFirebase.setListener(downloadingListener);
                myFirebase.downloadFile(Global.pathFiles.getPath() + "/", listVersionServer.get(i));
            } else {
                DownloadFileFromServer myDownload = new DownloadFileFromServer();
                myDownload.setDownloadingListener(downloadingListener);
                myDownload.downloadFile(Global.urlServer, Global.pathFiles.getPath() + "/", listVersionServer.get(i));
            }
        } else {
            endUpdate(true);
        }
    }

    private void execAfterDownloadFile(File file, boolean isFirebase) {
        boolean unzip = FileProcessor.unzipFile(file.getPath(), file.getParent() + "/");
        Log.d("uzip", file.toString() + " - " + unzip);

        boolean deleteFileZip = FileProcessor.deleteFile(file);
        Log.i("delete", file.toString() + " - " + deleteFileZip);

        // Kiểm tra có cập nhật file database không?
        File fileDatabaseCache = new File(file.getParent() + "/devkid.db");
        boolean checkFileDatabase = FileProcessor.checkExistsFile(fileDatabaseCache);
        Log.i("checkExsits", fileDatabaseCache.getPath() + " - " + checkFileDatabase);

        // tiến hành sao lưu và cập nhật database
        if (checkFileDatabase) {
            ArrayList<TopicItem> topicItems = new ArrayList<TopicItem>();
            ArrayList<TopicName> topicNames = new ArrayList<TopicName>();

            // nếu app đã có tài nguyên lưu lại database
            if (!Global.resourceVersion.equals(Global.NO_DATA_KEY)) { // nếu app đã có tài nguyên
                DBHelper dbHelper = DBHelper.getInstance(context);
                if (dbHelper.isCreatedDatabase()) {
                    dbHelper.openDataBase();

                    topicItems = dbHelper.getTopicItemForUpdater();
                    Log.i("ListItem", topicItems.get(0).getId() + "-" + topicItems.get(0).getShow_ratio() + "-" + topicItems.get(0).getCorrect_count());

                    topicNames = dbHelper.getTopicNameForUpdater();
                    Log.i("ListName", topicNames.get(0).getId() + "-" + topicNames.get(0).getPercent());

                    dbHelper.close();
                }
            }

            // coppy file database từ unzip folder vào data folder
            FileProcessor.copyFile(fileDatabaseCache.getPath(), Global.pathSysDatabase.getPath());

            // thêm dữ liệu database cũ vào database mới
            if (!Global.resourceVersion.equals(Global.NO_DATA_KEY)) { // nếu app đã có tài nguyên
                DBHelper dbHelper1 = DBHelper.getInstance(context);
                dbHelper1.openDataBase();
                if (!topicItems.isEmpty()) {
                    for (TopicItem topicItem : topicItems) {
                        dbHelper1.updateTopicItem(topicItem.getId(), topicItem.getShow_ratio(), topicItem.getCorrect_count());
                    }
                    topicItems.clear();
                }
                if (!topicNames.isEmpty()) {
                    for (TopicName topicName : topicNames) {
                        dbHelper1.updateTopicName(topicName.getId(), topicName.getPercent(), topicName.getLock());
                    }
                    topicNames.clear();
                }
                dbHelper1.close();
            }

            boolean deleteFileDatabase = FileProcessor.deleteFile(fileDatabaseCache);
            Log.i("delete", fileDatabaseCache.getPath() + " - " + deleteFileDatabase);
        }

        // cập nhật phiên bản client
        String currentVersion = file.getName();
        Log.d("currentVersion", currentVersion);

        ResourceVersion resourceVersion = new ResourceVersion(context);
        resourceVersion.setResourceVersion(currentVersion);
        Log.d("resourceVersion", Global.resourceVersion);

        downloadFile(listVersionServer.indexOf(currentVersion) + 1, isFirebase);
    }

    private void endUpdate(Boolean aBoolean) {
        publishProgress(100);
        if (aBoolean) {
            updaterListener.updateOK();
        } else {
            updaterListener.updateFail(isFirebase);
        }
    }

    private void updateProgress(Integer add) {
        publishProgress(add);
    }

    public void setUpdaterListener(UpdaterListener updaterListener) {
        this.updaterListener = updaterListener;
    }

}

