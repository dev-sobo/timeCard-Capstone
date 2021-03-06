package com.example.ian.timecardcapstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mTracker = Main2Activity.app.getDefaultTracker();

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "SETTING SCREENANME");
        mTracker.setScreenName("SETTINGS_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}