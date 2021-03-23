package com.kabasonic.notepad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.kabasonic.notepad.ui.onboarding.ScreenSlidePagerActivity;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check and start on boarding fragments
        checkOnBoarding();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.topAppBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(mToolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.remindersFragment,
                R.id.favoriteFragment,
                R.id.trashFragment,
                R.id.helpFragment)
                .setDrawerLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        int modeDisplayView = (sharedPref.getInt(getResources().getString(R.string.saved_displaying_elements), 0));
        int modeDisplayText = (sharedPref.getInt(getResources().getString(R.string.saved_displaying_text_note), 0));
        Log.d(getClass().getSimpleName(), "Status " + modeDisplayText);
        if (modeDisplayView == 2)
            menu.findItem(R.id.display_plate).setChecked(true);
        if (modeDisplayView == 1)
            menu.findItem(R.id.display_row).setChecked(true);
        if (modeDisplayText == 0)
            menu.findItem(R.id.show_text_notes_off).setChecked(true);
        if (modeDisplayText == 1)
            menu.findItem(R.id.show_text_notes_on).setChecked(true);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (item.getItemId()) {
            case R.id.sorted_personally:
                Log.d("Menu", "Sorting: Personally");
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.sorted_date:
                Log.d("Menu", "Sorting: By date of change");
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.sorted_title:
                Log.d("Menu", "Sorting: By title");
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.display_plate:
                Log.d("Menu", "Displaying elements: Plate");
                editor.putInt(getResources().getString(R.string.saved_displaying_elements), 2);
                editor.apply();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.display_row:
                Log.d("Menu", "Displaying elements: Row");
                editor.putInt(getResources().getString(R.string.saved_displaying_elements), 1);
                editor.apply();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.show_text_notes_on:
                editor.putInt(getResources().getString(R.string.saved_displaying_text_note), 1);
                editor.apply();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
            case R.id.show_text_notes_off:
                editor.putInt(getResources().getString(R.string.saved_displaying_text_note), 0);
                editor.apply();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkOnBoarding() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.shared_preferences_notepad), MODE_PRIVATE);
        int statusOnBoarding = sharedPreferences.getInt(getResources().getString(R.string.saved_on_boarding_status_key), 0);
        if (statusOnBoarding != 1) {
            Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
            startActivity(intent);
        }
    }
}