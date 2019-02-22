package vn.devpro.devprokidorigin.interfaces;

import java.io.File;

public interface DownloadingListener {
    void downloadProgress(Double percent);

    void downloadOK(File pathFile);

    void downloadFail();
}
