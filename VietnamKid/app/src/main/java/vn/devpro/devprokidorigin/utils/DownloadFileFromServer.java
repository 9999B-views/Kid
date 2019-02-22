package vn.devpro.devprokidorigin.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import vn.devpro.devprokidorigin.interfaces.DownloadingListener;

public class DownloadFileFromServer {
    private String TAG = "DownloaFileTask";
    private DownloadingListener downloadingListener;

    public void setDownloadingListener(DownloadingListener downloadingListener) {
        this.downloadingListener = downloadingListener;
    }

    public DownloadFileFromServer() {
    }

    public void downloadFile(String serverLink, String targetFilePath, String fileName) {
        DownloaFileTask downloaFileTask = new DownloaFileTask();
        String[] strings = new String[2];
        strings[0] = serverLink + fileName;
        strings[1] = targetFilePath + fileName;
        downloaFileTask.execute(strings);
    }

    private class DownloaFileTask extends AsyncTask<String, Double, File> {

        /**
         * Downloading file in background thread
         */
        @Override
        protected File doInBackground(String... strings) {
            File file = null;
            int count;
            try {
                URL url = new URL(strings[0]);
                URLConnection conection = url.openConnection();
                conection.setConnectTimeout(3000);
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                file = new File(strings[1]);

                // Output stream to write file
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(1.0 * total / lenghtOfFile);

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }

            return file;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(Double... progress) {
            downloadingListener.downloadProgress(progress[0]);
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(File file) {
            try {
                if (file != null) {
                    downloadingListener.downloadOK(file);
                    Log.e(TAG, file.getPath() + " - OK");
                } else {
                    downloadingListener.downloadFail();
                    Log.e(TAG, " - Fail");
                }
            } catch (Exception e) {
                downloadingListener.downloadFail();
                Log.e(TAG, file.getPath() + " - Fail");
            }
        }
    }
}
