package com.timsoft.meurebanho.animal.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.infra.MoneyTextWatcher;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.race.model.RaceArrayAdapter;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class AnimalMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AnimalMaintainActivity";

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICTURE_CROP_ACTIVITY_REQUEST_CODE = 200;
    private static final int PICTURE_SELECT_ACTIVITY_REQUEST_CODE = 300;

    private Spinner racesSpinner;
    private Specie includingSpecie;
    private Animal editingAnimal;
    private File tempPicture, picture;
    private TextView tvId, tvBirthDate, tvAquisitionDate;
    private EditText etAcquisitionValue, etSellerName;
    private ImageView imageViewPicture;
    private String action;

    private DBAnimalAdapter animalDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton btnClearBirthDate, btnClearAquisitionDate;

        animalDatasource = DBAnimalAdapter.getInstance();
        DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.check);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.animal_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            //Caso seja uma inclusão de animal
            int idSpecie = getIntent().getExtras().getInt(DBSpecieAdapter.ID);

            specieDatasource.open();
            includingSpecie = specieDatasource.get(idSpecie);
            specieDatasource.close();

        } else {
            //Caso seja uma edição de animal
            int idAnimal = getIntent().getExtras().getInt(DBAnimalAdapter.ID);

            animalDatasource.open();
            editingAnimal = animalDatasource.get(idAnimal);
            animalDatasource.close();

            specieDatasource.open();
            includingSpecie = specieDatasource.get(editingAnimal.getSpecieId());
            specieDatasource.close();
        }

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.add) + " " + includingSpecie.getDescription());
        } else {
            setTitle(getResources().getString(R.string.edit_animal));
        }
        //

        //id
        tvId = (TextView) findViewById(R.id.am_id);
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            animalDatasource.open();
            tvId.setText(Integer.toString(animalDatasource.getNextId()));
            animalDatasource.close();
        } else {
            tvId.setText(editingAnimal.getIdToDisplay());
        }
        //

        //Name
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            ((TextView) findViewById(R.id.am_name)).setText(editingAnimal.getName());
        }
        //

        //Sex
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            if ("M".equalsIgnoreCase(editingAnimal.getSex())) {
                ((RadioButton) findViewById(R.id.am_sex_male)).setChecked(true);
                ((RadioButton) findViewById(R.id.am_sex_female)).setChecked(false);
            } else {
                ((RadioButton) findViewById(R.id.am_sex_male)).setChecked(false);
                ((RadioButton) findViewById(R.id.am_sex_female)).setChecked(true);
            }
        }

        //Races
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
        raceDatasource.open();
        List<Race> races = raceDatasource.listBySpecieId(includingSpecie.getId());
        raceDatasource.close();

        racesSpinner = (Spinner) findViewById(R.id.am_race);
        racesSpinner.setAdapter(new RaceArrayAdapter(this, races));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            int pos = -1;
            for (int i = 0; i < races.size(); i++) {
                if (races.get(i).getId() == editingAnimal.getRaceId()) {
                    pos = i;
                    break;
                }
            }
            if (pos == -1) {
                throw new RuntimeException("Id de espécie não localizada: " + editingAnimal.getRaceId());
            }
            racesSpinner.setSelection(pos);
        }
        //

        //Ear tag
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            ((EditText) findViewById(R.id.am_ear_tag)).setText(editingAnimal.getEarTag());
        }
        //

        //Birth Date
        tvBirthDate = (TextView) findViewById(R.id.am_birth_date);
        tvBirthDate.setOnClickListener(getOnClickListenerForBtnSetDate(tvBirthDate));
        btnClearBirthDate = (ImageButton) findViewById(R.id.am_clear_birth_date);
        btnClearBirthDate.setOnClickListener(getOnClickListenerForBtnClearDate(tvBirthDate, R.string.animal_birth_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            updateDate(tvBirthDate, editingAnimal.getBirthDate());
        }
        //

        //Aquisition Date
        tvAquisitionDate = (TextView) findViewById(R.id.am_acquisition_date);
        tvAquisitionDate.setOnClickListener(getOnClickListenerForBtnSetAquisitionDate(tvAquisitionDate));
        btnClearAquisitionDate = (ImageButton) findViewById(R.id.am_clear_acquisition_date);
        btnClearAquisitionDate.setOnClickListener(getOnClickListenerForBtnClearAquisitionDate(tvAquisitionDate, R.string.animal_acquisition_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT) && editingAnimal.getAcquisitionDate() != null) {
            updateDate(tvAquisitionDate, editingAnimal.getAcquisitionDate());
        }
        //

        //Aquisition Value
        etAcquisitionValue = (EditText) findViewById(R.id.am_acquisition_value);

        etAcquisitionValue.addTextChangedListener(new MoneyTextWatcher(etAcquisitionValue));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT) && editingAnimal.getAcquisitionDate() != null) {
            etAcquisitionValue.setText(NumberFormat.getCurrencyInstance().format(editingAnimal.getAcquisitionValue()));
        } else {
            etAcquisitionValue.setEnabled(false);
        }

        //Seller Name
        etSellerName = (EditText) findViewById(R.id.am_seller_name);

        if (action.equals(MeuRebanhoApp.ACTION_EDIT) && editingAnimal.getAcquisitionDate() != null) {
            etSellerName.setText(editingAnimal.getSellerName());
        } else {
            etSellerName.setEnabled(false);
        }

        imageViewPicture = (ImageView) findViewById(R.id.am_picture);
        imageViewPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        registerForContextMenu(imageViewPicture);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT) && editingAnimal.getPictureFile().exists()) {
            picture = getOutputMediaFile();
            MeuRebanhoApp.copy(editingAnimal.getPictureFile(), picture);
            updateImageViewPicture();
        }
    }

    private OnClickListener getOnClickListenerForBtnSetDate(final TextView tvDate) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDateSetListener listener = new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        updateDate(tvDate, year, monthOfYear, dayOfMonth);
                    }
                };

                //TODO: Se já houver uma data digitada, exibir esta data no calendário, default está como data de hoje
                DatePickerDialog d = new DatePickerDialog(AnimalMaintainActivity.this, listener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        };
    }

    private OnClickListener getOnClickListenerForBtnSetAquisitionDate(final TextView tvDate) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDateSetListener listener = new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        updateDate(tvDate, year, monthOfYear, dayOfMonth);
                        etSellerName.setEnabled(true);
                        etAcquisitionValue.setEnabled(true);
                    }
                };

                //TODO: Se já houver uma data digitada, exibir esta data no calendário, default está como data de hoje
                DatePickerDialog d = new DatePickerDialog(AnimalMaintainActivity.this, listener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        };
    }

    private OnClickListener getOnClickListenerForBtnClearDate(final TextView tvDate, final int hint_id) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setTextColor(getResources().getColor(R.color.hintTextAppearance));
                tvDate.setText(getResources().getString(hint_id));
            }
        };
    }

    private OnClickListener getOnClickListenerForBtnClearAquisitionDate(final TextView tvDate, final int hint_id) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setTextColor(getResources().getColor(R.color.hintTextAppearance));
                tvDate.setText(getResources().getString(hint_id));
                etAcquisitionValue.setText("");
                etAcquisitionValue.setEnabled(false);
                etSellerName.setText("");
                etSellerName.setEnabled(false);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintain_actions, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.am_picture) {
            if (picture == null) {
                getMenuInflater().inflate(R.menu.take_or_select_picture_context_menu, menu);
            } else {
                getMenuInflater().inflate(R.menu.remove_take_other_or_select_other_picture_context_menu, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_picture:
            case R.id.action_take_other_picture:
                takePicture();
                return true;

            case R.id.action_select_picture:
            case R.id.action_select_other_picture:
                selectPictureFromGallery();
                return true;

            case R.id.action_remove_picture:
                if (!picture.delete()) {
                    Toast.makeText(this, getResources().getString(R.string.error_deleting_file) + ":" + picture.getName(), Toast.LENGTH_SHORT).show();
                }
                picture = null;
                updateImageViewPicture();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    //TODO: Verificar se a data realmente está certa
    private void updateDate(TextView tv, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int monthOfYear = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        updateDate(tv, year, monthOfYear, dayOfMonth);
    }

    @SuppressWarnings("deprecation")
    private void updateDate(TextView tv, int year, int monthOfYear, int dayOfMonth) {
        DateFormat f = MainActivity.getDateFormat();
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        tv.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        tv.setText(f.format(c.getTime()));
    }

    private void takePicture() {
        try {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            tempPicture = getOutputMediaFile();

            Uri fileUri = Uri.fromFile(tempPicture); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, getResources().getString(R.string.camera_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICTURE_SELECT_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Create a File for saving an image or video
     */
    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile() {
        //TODO:
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//	              Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_full_name));

        File mediaStorageDir = MeuRebanhoApp.getMediaStorageDir();
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(getResources().getString(R.string.app_full_name), "failed to create directory");
                return null;
            }
        }

        // Create a temporary media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "TMP_" + timeStamp + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //user is returning from camera app
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                performCrop();
            } else if (resultCode == RESULT_CANCELED) {
                //Não faz nada, usuário cancelou a ação
            } else {
                Toast.makeText(this, LOG_TAG + " - Unrecognized resultCode: " + resultCode, Toast.LENGTH_LONG).show();
            }

            //user is selecting image from gallery
        } else if (requestCode == PICTURE_SELECT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Copia o arquivo original para dentro da pasta de imagens do aplicativo
                Uri selectedPictureUri = data.getData();
                tempPicture = getOutputMediaFile();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedPictureUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                File originalPicture = new File(picturePath);
                cursor.close();

                MeuRebanhoApp.copy(originalPicture, tempPicture);

                performCrop();
            } else if (resultCode == RESULT_CANCELED) {
                //Não faz nada, usuário cancelou a ação
            } else {
                Toast.makeText(this, LOG_TAG + " - Unrecognized resultCode: " + resultCode, Toast.LENGTH_LONG).show();
            }

            //user is returning from cropping the image
        } else if (requestCode == PICTURE_CROP_ACTIVITY_REQUEST_CODE) {
            //get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap
            saveBitmapToFile((Bitmap) extras.getParcelable("data"), tempPicture);
        } else {
            Toast.makeText(this, LOG_TAG + " - Unrecognized requestCode: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    private void saveBitmapToFile(Bitmap bmp, File file) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            picture = tempPicture;
            updateImageViewPicture();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            //indicate image type and Uri
            cropIntent.setDataAndType(Uri.fromFile(tempPicture), "image/*");

            //set crop properties
            cropIntent.putExtra("crop", "true");

            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);

            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);

            //retrieve data on return
            cropIntent.putExtra("return-data", true);

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PICTURE_CROP_ACTIVITY_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            //Dont crop, picture will be added "as is"
            picture = tempPicture;
            updateImageViewPicture();
        }
    }

    private void updateImageViewPicture() {
        if (picture != null) {
            imageViewPicture.setImageBitmap(BitmapFactory.decodeFile(picture.getPath()));
        } else {
            imageViewPicture.setImageResource(R.drawable.cow);
        }
    }

    private void save() {
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            editingAnimal = new Animal();
        }

        animalDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            // id
            editingAnimal.setId(Integer.parseInt((tvId.getText().toString())));
            //

            //includingSpecie
            editingAnimal.setSpecieId(includingSpecie.getId());
            //
        }

        //race
        Race selectedRace = (Race) racesSpinner.getSelectedItem();
        if (selectedRace == null) {
            Toast.makeText(this, R.string.race_not_selected, Toast.LENGTH_SHORT).show();
            return;
        } else {
            editingAnimal.setRaceId(selectedRace.getId());
        }
        //

        //sex
        if (((RadioButton) findViewById(R.id.am_sex_male)).isChecked()) {
            editingAnimal.setSex("M");
        } else if (((RadioButton) findViewById(R.id.am_sex_female)).isChecked()) {
            editingAnimal.setSex("F");
        }

        if (editingAnimal.getSex() == null) {
            Toast.makeText(this, R.string.sex_not_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Name
        editingAnimal.setName(((EditText) findViewById(R.id.am_name)).getText().toString().trim());
        //

        //Ear tag
        editingAnimal.setEarTag(((EditText) findViewById(R.id.am_ear_tag)).getText().toString().trim());
        //

        //Birth date
        try {
            editingAnimal.setBirthDate(MainActivity.getDateFormat().parse(tvBirthDate.getText().toString()));
        } catch (java.text.ParseException e) {
            Toast.makeText(this, R.string.birth_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Aquisition date
        //Test if there is numbers typed (it is not empty because the placeholder text)
        editingAnimal.setAcquisitionDate(null);
        editingAnimal.setAcquisitionValue(0);
        editingAnimal.setSellerName(null);
        if (!TextUtils.isEmpty(tvAquisitionDate.getText().toString().replaceAll("[^\\d]", ""))) {
            try {
                editingAnimal.setAcquisitionDate(MainActivity.getDateFormat().parse(tvAquisitionDate.getText().toString()));
            } catch (java.text.ParseException e) {
                Toast.makeText(this, R.string.aquisition_date_invalid, Toast.LENGTH_SHORT).show();
                return;
            }

            //Aquisition value
            try {
                double aquisitionValue = NumberFormat.getCurrencyInstance().parse(etAcquisitionValue.getText().toString()).doubleValue();
                if (aquisitionValue <= 0) {
                    Toast.makeText(this, R.string.aquisition_value_invalid, Toast.LENGTH_SHORT).show();
                    return;
                }
                editingAnimal.setAcquisitionValue(aquisitionValue);
            } catch (ParseException e) {
                Toast.makeText(this, R.string.aquisition_value_invalid, Toast.LENGTH_SHORT).show();
                return;
            }

            //Seller name
            if (!TextUtils.isEmpty(etSellerName.getText().toString())) {
                editingAnimal.setSellerName(etSellerName.getText().toString());
            } else {
                Toast.makeText(this, R.string.seller_name_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        animalDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            animalDatasource.create(editingAnimal);
        } else {
            animalDatasource.update(editingAnimal);
        }

        animalDatasource.close();

        //Remove current file if exists
        if (editingAnimal.getPictureFile().exists()) {
            if (!editingAnimal.getPictureFile().delete()) {
                Toast.makeText(this, getResources().getString(R.string.error_deleting_file) + ":" + editingAnimal.getPictureFile().getName(), Toast.LENGTH_SHORT).show();
            }
        }

        //If a picture was set, rename it to the correct animal picture file name
        if (picture != null) {
            if (!picture.renameTo(editingAnimal.getPictureFile())) {
                throw new RuntimeException("Falha ao renomear arquivo");
            }
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Substitui a ação de voltar pela ação de salvar
            case android.R.id.home:
                save();
                break;

            //Cancela a inclusão de editingAnimal
            case R.id.action_discard_changes:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}