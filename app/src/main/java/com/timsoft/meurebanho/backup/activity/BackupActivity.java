package com.timsoft.meurebanho.backup.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackupActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BackupDetailActivity";

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
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "<package name>"
                        + "//databases//" + "<database name>";
                String backupDBPath = "<backup db filename>"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Import Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void create() {
        if (!FileUtils.getBackupStorageDir().canWrite()) {
            Toast.makeText(getApplicationContext(), "Não foi possível gravar no cartão de memória!", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String zipFile = FileUtils.getBackupStorageDir().toString() + File.separator + timeStamp + "_"
                + MeuRebanhoApp.getContext().getString(R.string.app_short_name) + ".zip";

        List<String> files = new ArrayList<>();

        files.add(getDatabasePath(DBHandler.DB_NAME).toString());

        for (File f : FileUtils.getMediaStorageDir().listFiles()) {
            files.add(f.toString());
        }

        FileUtils.zip(files, zipFile);

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