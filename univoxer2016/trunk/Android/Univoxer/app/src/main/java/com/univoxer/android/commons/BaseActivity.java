package com.univoxer.android.commons;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.univoxer.android.R;

/**
 * Created by Gustavo on 01/06/2016.
 * BaseActivity
 */
public class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.base_activity);
    }

    public void addFragment(Fragment fragment, int container, boolean backStack) {
        addFragment(fragment, container, null, backStack);
    }

    public void addFragment(Fragment fragment, int container, Bundle bundle, boolean backStack) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (backStack)
            ft.addToBackStack(fragment.getClass().getName());

        ft.add(container, fragment);
        ft.commitAllowingStateLoss();
    }

    public void switchFragment(Fragment fragment, int container, boolean backStack) {
        switchFragment(fragment, container, null, backStack);
    }

    public void switchFragment(Fragment fragment, int container, Bundle bundle, boolean backStack) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        if (backStack)
            ft.addToBackStack(fragment.getClass().getName());

        ft.replace(container, fragment);
        ft.commit();
    }


}
