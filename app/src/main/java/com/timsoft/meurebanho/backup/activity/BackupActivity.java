package com.timsoft.meurebanho.backup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.infra.FileUtils;
import com.timsoft.meurebanho.infra.db.DBHandler;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackupActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BackupDetailActivity";

    public static final String BACKUP_FILE_EXTENSION = ".zip";

    private static final int OPTION_DOWNLOADS = 0;
    private static final int OPTION_INTERNAL_DIRECTORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.backup_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.bk_create).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        create();
                    }
                });

        findViewById(R.id.bk_restore).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restore();
                    }
                });
    }

    private void restore() {
        Log.d(LOG_TAG, "Click!");

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_restore_origin)
                .setItems(R.array.restore_backup_sources, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case OPTION_INTERNAL_DIRECTORY:
                                showSelectRestoreFileDialog(FileUtils.getBackupStorageDir());
                                break;
                            case OPTION_DOWNLOADS:
                                showSelectRestoreFileDialog(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Import Successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private void showSelectRestoreFileDialog(final File backupStorageDir) {
        final FilenameFilter bkpFilenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase().endsWith("_" + MeuRebanhoApp.getContext().getString(R.string.app_short_name) + BACKUP_FILE_EXTENSION);
            }
        };

        final String[] files = backupStorageDir.list(bkpFilenameFilter);

        if (files.length == 0) {
            Toast.makeText(getApplicationContext(), "Não foram encontrados arquivos de backup para restauração", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_restore_file)
                .setItems(files, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int which) {
                        new AlertDialog.Builder(BackupActivity.this)
                                .setTitle(R.string.confirm_restore)
                                .setMessage(R.string.all_data_lost)
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                restoreBackup(backupStorageDir + File.separator + files[which]);
                                            }
                                        })
                                .setNegativeButton(R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        })
                                .show();
                    }
                })
                .show();
    }

    private void restoreBackup(String backupFilePath) {
        //Apagando os arquivos na pasta temporária
        for (File f : FileUtils.getTemporaryStorageDir().listFiles()) {
            f.delete();
        }

        if (!FileUtils.unzip(FileUtils.getTemporaryStorageDir().toString(), backupFilePath)) {
            return;
        }

        File bkpDB = new File(FileUtils.getTemporaryStorageDir() + File.separator + DBHandler.DB_NAME);

        if (!bkpDB.exists()) {
            Toast.makeText(this, "Não há arquivo de banco de dados no backup!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Apagando os arquivos de imagem atuais
        for (File f : FileUtils.getMediaStorageDir().listFiles()) {
            f.delete();
        }

        //Apagando banco de dados atual
        deleteDatabase(DBHandler.DB_NAME);

        //Copiando imagens do backup para a pasta de imagens
        for (File f : FileUtils.getTemporaryStorageDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase().endsWith("*" + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION);
            }
        })) {
            FileUtils.copy(f, new File(FileUtils.getMediaStorageDir() + File.separator + f.toString()));
        }

        //Restaurando o arquivo do banco de dados
        FileUtils.copy(bkpDB, new File(getDatabasePath(DBHandler.DB_NAME).toString()));

        //Apagando os arquivos na pasta temporária
        for (File f : FileUtils.getTemporaryStorageDir().listFiles()) {
            f.delete();
        }

        Toast.makeText(this, "Backup restaurado com sucesso!",
                Toast.LENGTH_LONG).show();

        finish();

        //Restart application
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void create() {
        if (!FileUtils.getBackupStorageDir().canWrite()) {
            Toast.makeText(getApplicationContext(), "Não foi possível gravar no cartão de memória!", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //Deleting temporary image files
        FileUtils.deleteTemporaryImageFiles();

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String zipFile = FileUtils.getBackupStorageDir().toString() + File.separator + timeStamp + "_"
                + MeuRebanhoApp.getContext().getString(R.string.app_short_name) + BACKUP_FILE_EXTENSION;

        List<String> files = new ArrayList<>();

        files.add(getDatabasePath(DBHandler.DB_NAME).toString());

        for (File f : FileUtils.getMediaStorageDir().listFiles()) {
            files.add(f.toString());
        }

        if (!FileUtils.zip(files, zipFile)) {
            return;
        }

        FileUtils.rescanApplicationStorageDir();

        Uri uriToZip = Uri.fromFile(new File(zipFile));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToZip);
        shareIntent.setType("application/zip");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

        Toast.makeText(getApplicationContext(), "Cópia de segurança criada com sucesso em: " + zipFile,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}