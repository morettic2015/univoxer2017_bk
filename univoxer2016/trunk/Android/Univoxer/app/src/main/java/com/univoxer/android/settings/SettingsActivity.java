package com.univoxer.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.univoxer.android.R;
import com.univoxer.android.commons.BaseActivity;
import com.univoxer.android.home.activity.HomeActivity;

/**
 * Created by Gustavo on 19/07/2016.
 * SettingsActivity
 */
public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.settings_title);

        addFragment(new SettingsFragment(), R.id.fragment_layout, true);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
