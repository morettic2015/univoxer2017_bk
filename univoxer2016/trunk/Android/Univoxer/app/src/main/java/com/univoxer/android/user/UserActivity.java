package com.univoxer.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.univoxer.android.R;
import com.univoxer.android.commons.BaseActivity;
import com.univoxer.android.home.activity.HomeActivity;

/**
 * Created by Gustavo on 27/07/2016.
 */
public class UserActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.profile_title);

        addFragment(new UserFragment(), R.id.fragment_layout, true);
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
