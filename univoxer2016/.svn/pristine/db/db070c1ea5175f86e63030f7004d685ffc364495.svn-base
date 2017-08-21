package com.univoxer.android.commons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.univoxer.android.R;

/**
 * Created by Gustavo on 02/06/2016.
 * BaseFragment
 */
public class BaseFragment extends Fragment {

    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    public void switchFragment(Fragment fragment, Bundle bundle, boolean backStack) {
        FrameLayout container = (FrameLayout) getActivity().findViewById(R.id.fragment_layout);

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        if(backStack)
            ft.addToBackStack(fragment.getClass().getName());

        ft.replace(container.getId(), fragment);
        ft.commit();
    }

    public void switchFragment(Fragment fragment, Bundle bundle, int container, boolean backStack) {

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        if(backStack)
            ft.addToBackStack(fragment.getClass().getName());

        ft.replace(container, fragment);
        ft.commit();
    }

    public void switchFragment(Fragment fragment, int container, boolean backStack) {
        switchFragment(fragment, null, container, backStack);
    }
}
