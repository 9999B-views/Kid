package vn.devpro.devprokidorigin.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by NamQuoc on 3/20/2018.
 */

public class FileProcessor {

    public static boolean copyFileFromAsset(Context context, String fileName, String targetPathFile) {
        try {
            InputStream myInput = context.getAssets().open(fileName);
            OutputStream myOutput = new FileOutputStream(targetPathFile);
            return copyFile(myInput, myOutput);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean copyFile(String pathFile, String targetPath) {
        try {
            InputStream myInput = new FileInputStream(pathFile);
            OutputStream myOutput = new FileOutputStream(targetPath);

            File f = new File(targetPath);
            if (!f.exists())
                f.mkdir();

            return copyFile(myInput, myOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(File pathFile, File targetPath) {
        try {
            InputStream myInput = new FileInputStream(pathFile);
            OutputStream myOutput = new FileOutputStream(targetPath);

            File f = new File(targetPath.getParent());
            if (!f.exists())
                f.mkdir();

            return copyFile(myInput, myOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(InputStream in, OutputStream out) {
        try {
            InputStream myInput = in;
            OutputStream myOutput = out;
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ArrayList<String> readFileText(File filePath) {
        ArrayList localArrayList = new ArrayList();
        File localFile = filePath;
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
            while (true) {
                String str = localBufferedReader.readLine();
                if (str == null)
                    break;
                localArrayList.add(str);
            }
            localBufferedReader.close();
            return localArrayList;
        } catch (IOException localIOException) {
        }
        return localArrayList;
    }

    public static void writeDataFileText(String filePath, String bodyText) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            writer.write(bodyText);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean unzipFile(String pathFile, String targetPath) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(pathFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(targetPath + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(targetPath + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


   /* public static void renameFile(File oldName,File newName){
        File dir = Environment.getExternalStorageDirectory();
        if(dir.exists()){
            File from = new File(dir,oldName);
            File to = new File(dir,newName);
            if(from.exists())
                from.renameTo(to);
        }
    }*/

    public static boolean deleteFile(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String[] listFile(String path) {
        File file = new File(path);
        return file.list();
    }

    public static boolean checkExistsFile(File pathFile) {
        if (pathFile.exists() && !pathFile.isDirectory()) {
            return true;
        }
        return false;
    }

    public static boolean checkExistsDir(File pathDir) {
        if (pathDir.exists() && pathDir.isDirectory()) {
            return true;
        }
        return false;
    }
}