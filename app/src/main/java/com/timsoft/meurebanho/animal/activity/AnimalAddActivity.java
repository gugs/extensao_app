package com.timsoft.meurebanho.animal.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.race.model.RaceArrayAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AnimalAddActivity extends AppCompatActivity {

	private static final String LOG_TAG = "AnimalAddActivity";

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICTURE_CROP_ACTIVITY_REQUEST_CODE = 200;
	private static final int PICTURE_SELECT_ACTIVITY_REQUEST_CODE = 300;

	private DBAnimalAdapter animalDatasource;

	private Spinner racesSpinner;
	private Specie specie;
	private File tempPicture, picture;
	private TextView tvId, tvAquisitionValue, tvBirthDate, tvAquisitionDate;
	private ImageButton btnClearBirthDate, btnClearAquisitionDate;
	private ImageView imageViewPicture;
	private double aquisitionValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.check);
        }

        setContentView(R.layout.animal_add_activity);

        //id
        tvId = (TextView) findViewById(R.id.aa_id);
        animalDatasource = DBAnimalAdapter.getInstance(this);
        animalDatasource.open();
        tvId.setText(Integer.toString(animalDatasource.getNextId()));
        animalDatasource.close();
        //

        //Specie
        specie = getIntent().getParcelableExtra(Specie.class.toString());
        setTitle(getResources().getString(R.string.add) + " " + specie.getDescription());
        //

        //Races
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance(this);
        raceDatasource.open();
        List<Race> races = raceDatasource.listBySpecieId(specie.getId());
        raceDatasource.close();

        racesSpinner = (Spinner) findViewById(R.id.aa_race);
        racesSpinner.setAdapter(new RaceArrayAdapter(this, races));
        //

        //Birth Date
        tvBirthDate = (TextView) findViewById(R.id.aa_birth_date);
        tvBirthDate.setOnClickListener(getOnClickListenerForBtnSetDate(tvBirthDate));
        btnClearBirthDate = (ImageButton) findViewById(R.id.aa_clear_birth_date);
        btnClearBirthDate.setOnClickListener(getOnClickListenerForBtnClearDate(tvBirthDate, R.string.animal_birth_date_hint));
        //

        //Aquisition Date
        tvAquisitionDate = (TextView) findViewById(R.id.aa_acquisition_date);
        tvAquisitionDate.setOnClickListener(getOnClickListenerForBtnSetDate(tvAquisitionDate));
        btnClearAquisitionDate = (ImageButton) findViewById(R.id.aa_clear_aquisition_date);
        btnClearAquisitionDate.setOnClickListener(getOnClickListenerForBtnClearDate(tvAquisitionDate, R.string.animal_aquisition_date_hint));
        //

        //Aquisition Value
        tvAquisitionValue = (TextView) findViewById(R.id.aa_aquisition_value);

        tvAquisitionValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    aquisitionValue = Double.parseDouble(editable.toString().replace(',', '.'));
                } catch (ParseException e) {
                    aquisitionValue = 0;
                }
            }
        });
        //

        imageViewPicture = (ImageView) findViewById(R.id.aa_picture);
        imageViewPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        registerForContextMenu(imageViewPicture);
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

                DatePickerDialog d = new DatePickerDialog(AnimalAddActivity.this, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        };
    }

	@SuppressWarnings("deprecation")
    private OnClickListener getOnClickListenerForBtnClearDate(final TextView tvDate, final int hint_id) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
				tvDate.setTextColor(getResources().getColor(R.color.hintTextAppearance));
                tvDate.setText(getResources().getString(hint_id));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_add_actions, menu);
        return true;
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);

		if(v.getId() == R.id.aa_picture) {
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
				picture.delete();
				picture = null;
				updateImageViewPicture();
				return true;
	            
	        default:
	            return super.onContextItemSelected(item);
	    }
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
		    
		} catch(ActivityNotFoundException anfe){
	        Toast.makeText(this, getResources().getString(R.string.camera_not_supported), Toast.LENGTH_SHORT).show();
	    }
	}
	
	private void selectPictureFromGallery() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, PICTURE_SELECT_ACTIVITY_REQUEST_CODE);
	}
	
	/** Create a File for saving an image or video */
	@SuppressLint("SimpleDateFormat")
	private File getOutputMediaFile(){
		//TODO:
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

//		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//	              Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_full_name));

        File mediaStorageDir = MeuRebanhoApp.getMediaStorageDir();
        // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()){
	        if (!mediaStorageDir.mkdirs()){
	            Log.d(getResources().getString(R.string.app_full_name), "failed to create directory");
	            return null;
	        }
	    }

	    // Create a temporary media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    return new File(mediaStorageDir.getPath() + File.separator + "TMP_"+ timeStamp + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION);
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
				Toast.makeText(this, "Unrecognized resultCode: " + resultCode, Toast.LENGTH_LONG).show();
			}
	        
	    //user is selecting image from gallery
	    } else if(requestCode == PICTURE_SELECT_ACTIVITY_REQUEST_CODE){
	    	if (resultCode == RESULT_OK) {
	    		//Copia o arquivo original para dentro da pasta de imagens do aplicativo
	    		Uri selectedPictureUri = data.getData();
	    		tempPicture = getOutputMediaFile();
	    		
	    		String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedPictureUri, filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            File originalPicture = new File(picturePath);
	            cursor.close();
	             
	    		copy(originalPicture, tempPicture);
	    		performCrop();
	    	} else if (resultCode == RESULT_CANCELED) {
				//Não faz nada, usuário cancelou a ação
			} else {
				Toast.makeText(this, "Unrecognized resultCode: " + resultCode, Toast.LENGTH_LONG).show();
			}
	    	
	    //user is returning from cropping the image
	    } else if(requestCode == PICTURE_CROP_ACTIVITY_REQUEST_CODE){
	    	//get the returned data
	    	Bundle extras = data.getExtras();
	    	//get the cropped bitmap
	    	saveBitmapToFile((Bitmap) extras.getParcelable("data"), tempPicture);
	    } else {
	    	Toast.makeText(this, "Unrecognized requestCode: " + requestCode, Toast.LENGTH_LONG).show();
	    }
	}
	
	public void copy(File src, File dst) {
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
		} catch(IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
		}
	}
	
	private void performCrop(){
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
		}
		catch(ActivityNotFoundException anfe){
		    //Dont crop, picture will be added "as is"
			picture = tempPicture;
			updateImageViewPicture();
		} 
	}
	
	private void updateImageViewPicture() {
		if(picture != null) {
			imageViewPicture.setImageBitmap(BitmapFactory.decodeFile(picture.getPath()));
		} else {
			imageViewPicture.setImageResource(R.drawable.cow);
		}
	}
	
    private void save() {
    	DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
    	Animal a = new Animal();
    	
    	// id
    	a.setId(Integer.parseInt((tvId.getText().toString())));
    	animalDatasource.open();
    	if(a.getId() == 0) {
    		Toast.makeText(this, R.string.alert_invalid_id, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	if(animalDatasource.get(a.getId()) != null) {
    		Toast.makeText(this, R.string.alert_id_already_used, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//
    	
    	//specie
    	a.setSpecieId(specie.getId());
    	//
    	
    	//race
    	Race selectedRace = (Race) racesSpinner.getSelectedItem();
    	if(selectedRace == null) {
    		Toast.makeText(this, R.string.race_not_selected, Toast.LENGTH_SHORT).show();
    		return;
    	} else {
    		a.setRaceId(selectedRace.getId());
    	}
    	//
    	
    	//sex
    	if(((RadioButton) findViewById(R.id.radio_add_animal_sex_male)).isChecked()) {
    		a.setSex("M");
    	} else if(((RadioButton) findViewById(R.id.radio_add_animal_sex_female)).isChecked()) {
    		a.setSex("F");
    	}
    	
    	if(a.getSex() == null){
    		Toast.makeText(this, R.string.sex_not_selected, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//
    	
    	//Name
    	a.setName(((EditText) findViewById(R.id.input_add_animal_name)).getText().toString().trim());
    	//
    	
    	//Ear tag
    	a.setEarTag(((EditText) findViewById(R.id.input_add_animal_ear_tag)).getText().toString().trim());
    	//
    	
    	//Birth date
    	try {
			a.setBirthDate(MainActivity.getDateFormat().parse(tvBirthDate.getText().toString()));
		} catch (java.text.ParseException e) {
			Toast.makeText(this, R.string.birth_date_invalid, Toast.LENGTH_SHORT).show();
			return;
		}
    	//
    	
    	//Aquisition date
		if (!getResources().getString(R.string.animal_aquisition_date_hint).equals(tvAquisitionDate.getText().toString())){
	    	try {
				a.setAquisitionDate(MainActivity.getDateFormat().parse(tvAquisitionDate.getText().toString()));
			} catch (java.text.ParseException e) {
				Toast.makeText(this, R.string.aquisition_date_invalid, Toast.LENGTH_SHORT).show();
				return;
			}
    	}
    	
    	//Aquisition value
    	a.setAquisitionValue(aquisitionValue);
    	//
    	
    	animalDatasource.open();
    	animalDatasource.create(a);
    	animalDatasource.close();
    	
    	if(picture != null) {
            //TODO: Clean
    		//File destFile = new File(picture.getAbsolutePath().replace(picture.getName(), a.getIdToDisplay() + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION));

            File destFile = a.getPictureFile();

            //Arquivo pode existir devido a alguma instalação anterior
            if(destFile.exists()) {
                destFile.delete();
            }

    		if(!picture.renameTo(destFile)){
                throw new RuntimeException("Falha ao renomear arquivo");
            }
    	}
    	
		goBack();
    }
    
    private void goBack() {
		//Going back to MainActivity
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			//Substitui a ação de voltar pela ação de salvar
			case android.R.id.home:
				save();
				break;

            //Cancela a inclusão de animal
            case R.id.action_discard_add_animal:
                goBack();
                break;

			default:
				return super.onOptionsItemSelected(item);
		}

        return true;
	}
}