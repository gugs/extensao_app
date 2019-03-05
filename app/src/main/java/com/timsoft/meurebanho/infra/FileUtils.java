package com.timsoft.meurebanho.infra;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    private static final int BUFFER = 2048;

    private static final String LOG_TAG = "FileUtils";

    private FileUtils() {
    }

    public static void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    public static void rescanApplicationStorageDir() {
        rescanApplicationStorageDir(getApplicationStorageDir().toString());
    }

    private static void rescanApplicationStorageDir(String dest) {
        // Scan files only (not folders);
        File[] files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        String[] paths = new String[files.length];
        for (int co = 0; co < files.length; co++) {
            paths[co] = files[co].getAbsolutePath();
        }

        MediaScannerConnection.scanFile(MeuRebanhoApp.getContext(), paths, null, null);

        // and now recursively scan subfolders
        files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (int co = 0; co < files.length; co++) {
            rescanApplicationStorageDir(files[co].getAbsolutePath());
        }
    }

    public static void deleteTemporaryImageFiles() {
        List<File> list = Arrays.asList(getMediaStorageDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(MeuRebanhoApp.TEMPORARY_FILE_PREFIX);
            }
        }));

        for (File f : list) {
            f.delete();
        }
    }

    public static boolean zip(List<String> files, String zipFile) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (String f : files) {
                Log.d(LOG_TAG, "Adding: " + f);
                FileInputStream fi = new FileInputStream(f);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(f.substring(f.lastIndexOf(File.separator) + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            String m = "Error compressing files: " + e;
            Log.e(LOG_TAG, m);
            Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean unzip(String dest, String zipFile) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[BUFFER];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(dest + File.separator + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(dest + File.separator + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            String m = "Error uncompressing file: " + e;
            Log.e(LOG_TAG, m);
            Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static File getMediaStorageDir() {
//        return new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), getContext().getResources().getString(R.string.app_full_name));
        File f = new File(getApplicationStorageDir(), "pictures");

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para armazenamento de mídia em: " + f.toString();
                Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }

    public static File getBackupStorageDir() {
        File f = new File(getApplicationStorageDir(), "backup");

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para cópia de segurança da aplicação: " + f.toString();
                Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }

    public static File getApplicationStorageDir() {
        File f = new File(Environment.getExternalStorageDirectory(),MeuRebanhoApp.getContext().getResources().getString(R.string.app_short_name));

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para armazenamento de dados da aplicação em: " + f.toString();
                Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }

    public static File getTemporaryStorageDir() {
        File f = new File(getApplicationStorageDir(), "tmp");

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para arquivos temporários da aplicação: " + f.toString();
                Toast.makeText(MeuRebanhoApp.getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }
}
