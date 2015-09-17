package com.timsoft.meurebanho.animal.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AnimalListFragmentPagerAdapter extends FragmentPagerAdapter{
	private int pageCount;
 
    public AnimalListFragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }
 
    @Override
    public Fragment getItem(int index) {
        return AnimalListFragment.newInstance(index);
    }
 
    @Override
    public int getCount() {
        return pageCount;
    }
}