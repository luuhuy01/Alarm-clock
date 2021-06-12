package com.luuhuy.btl_donghobaothuc.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentNavigationAdapter extends FragmentStatePagerAdapter {
    private int count = 2;

    public FragmentNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new AlarmFragment();
            case 1: return new StopwatchFragment();
            default: return new AlarmFragment();
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
