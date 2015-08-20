package com.timsoft.meurebanho.specie.model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timsoft.meurebanho.R;
//FIXME: Classe não mais utilizada
public class SpecieArrayAdapter extends ArrayAdapter<Specie> {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "SpecieArrayAdapter";
	
	private static Map<Integer, Integer> resourceMap;

	public SpecieArrayAdapter(Context context, List<Specie> values) {
		super(context, R.layout.specie_list, values);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView);
	}
	
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }
    
    private View initView(int position, View convertView) {
        if(convertView == null)
            convertView = View.inflate(getContext(),
                                       R.layout.specie_list,
                                       null);
        ((TextView)convertView.findViewById(R.id.specie_list_name)).setText(getItem(position).getDescription());
        
		InputStream bm = convertView.getResources().openRawResource(getResourceFomId(getItem(position).getId()));
		BufferedInputStream bufferedInputStream = new BufferedInputStream(bm);
		Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
		int nh = (int) (bmp.getHeight() * (128.0 / bmp.getWidth()));
		bmp = Bitmap.createScaledBitmap(bmp, 128, nh, true);
		ImageView imgViewSpecie = (ImageView) convertView.findViewById(R.id.specie_list_image);
		imgViewSpecie.setImageBitmap(bmp);
        
        return convertView;
    }
    
    private static int getResourceFomId(int id){
    	if(resourceMap == null) {
    		resourceMap = new HashMap<Integer, Integer>();
    		resourceMap.put(1, R.raw.specie_1);
    		resourceMap.put(2, R.raw.specie_2);
    		resourceMap.put(3, R.raw.specie_3);
    		resourceMap.put(4, R.raw.specie_4);
    		resourceMap.put(5, R.raw.specie_5);
    	}
    	return resourceMap.get(id);
    }
	
}
