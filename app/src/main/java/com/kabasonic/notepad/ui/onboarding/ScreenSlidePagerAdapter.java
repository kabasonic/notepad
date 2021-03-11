package com.kabasonic.notepad.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public static final int NUM_PAGES = 3;
    private int currentItem = 0;

    public ScreenSlidePagerAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ScreenSlidePageFragment(position);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
