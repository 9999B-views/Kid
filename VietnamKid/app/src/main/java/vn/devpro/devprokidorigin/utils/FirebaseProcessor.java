package vn.devpro.devprokidorigin.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import vn.devpro.devprokidorigin.interfaces.DownloadingListener;

/**
 * Created by NamQuoc on 3/20/2018.
 */

public class FirebaseProcessor {
    private DownloadingListener downloadingListener;

    public void setListener(DownloadingListener downloadingListener) {
        this.downloadingListener = downloadingListener;
    }

    public FirebaseProcessor() {
    }

    public void downloadFile(String targetPath, String child) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorage.setMaxDownloadRetryTimeMillis(30000);
        StorageReference mStorageRef = firebaseStorage.getReference();
        StorageReference riversRef = mStorageRef.child(child);

        final File localFile = new File(targetPath, child);
        riversRef.getFile(localFile)
                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    double progress = 1.0;

                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        progress = 1.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();

                        //DecimalFormat df = new DecimalFormat("#.##");
                        //String S = df.format(progress);
                        //progress = Double.parseDouble(S);
                        if (downloadingListener != null)
                            downloadingListener.downloadProgress(progress);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        Log.i("downloadFile", localFile.getPath() + " - OK");
                        downloadingListener.downloadOK(localFile);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        Log.e("downloadFile", localFile.getPath() + " - Fail");
                        downloadingListener.downloadFail();
                    }
                });
    }
}
