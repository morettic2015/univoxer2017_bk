package com.univoxer.android.login.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.univoxer.android.R;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.login.activity.LoginActivity;
import com.univoxer.android.user.User;
import com.univoxer.android.network.RequestManager;

/**
 * Created by Gustavo on 18/06/2016.
 * SignUpThirdPageFragment
 */
public class SignUpThirdPageFragment extends BaseFragment implements View.OnClickListener {
    private Button mButton;
    private RadioButton mRadioUser;
    private RadioButton mRadioTranslate;

    private static final String URL_EULA = "http://www.univoxer.com/eula/";

    private static final String ROLE_USER = "1";
    private static final String ROLE_TRANSLATE = "2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_third_page_fragment, container, false);

        User.getInstance().setUserData(RequestManager.ROLE, ROLE_USER);

        mButton = (Button) view.findViewById(R.id.btn_next);
        mRadioUser = (RadioButton) view.findViewById(R.id.radio_user);
        mRadioTranslate = (RadioButton) view.findViewById(R.id.radio_translate);

        mButton.setOnClickListener(this);
        mRadioUser.setOnClickListener(this);
        mRadioTranslate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        LoginActivity loginActivity = (LoginActivity) getActivity();

        switch (view.getId()) {
            case R.id.layout_EULA:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URL_EULA));
                startActivity(i);
                break;
            case R.id.radio_user:
                if (((RadioButton) view).isChecked()) {
                    User.getInstance().setUserData(RequestManager.ROLE, ROLE_USER);
                    mButton.setText(R.string.btn_finish);
                }
                break;
            case R.id.radio_translate:
                if (((RadioButton) view).isChecked()) {
                    User.getInstance().setUserData(RequestManager.ROLE, ROLE_TRANSLATE);
                    mButton.setText(R.string.btn_next);
                }
                break;
            case R.id.btn_next:
                if (mRadioTranslate.isChecked()) {
                    loginActivity.createUser(false);
                    switchFragment(new SignUpFourthPageFragment(), null, true);
                } else {
                    loginActivity.createUser(true);
                }
                break;
        }
    }
}
