package com.kabasonic.notepad.ui.onboarding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.kabasonic.notepad.R;

public class ScreenSlidePagerActivity extends FragmentActivity implements ScreenSlidePageFragment.EventOnBoarding {
    private ViewPager2 viewPager2;

    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        viewPager2 = findViewById(R.id.viewPager2);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    @Override
    public void event() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getResources().getString(R.string.saved_on_boarding_status_key), 1);
        editor.apply();
        Log.i(getClass().getSimpleName(), " On boarding is shown.");

        finish();
    }
}
