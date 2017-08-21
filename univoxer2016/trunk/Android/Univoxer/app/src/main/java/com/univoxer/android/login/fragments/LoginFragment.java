package com.univoxer.android.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.univoxer.android.R;
import com.univoxer.android.utils.SharedPrefsUtil;
import com.univoxer.android.utils.UnivoxerLog;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.home.activity.HomeActivity;
import com.univoxer.android.user.User;
import com.univoxer.android.network.NetworkManager;
import com.univoxer.android.network.NetworkRequestCallback;
import com.univoxer.android.network.RequestManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo on 01/06/2016.
 * LoginFragment
 */
public class LoginFragment extends BaseFragment {

    private static String TAG = LoginFragment.class.getSimpleName();

    private EditText mEmail;
    private EditText mPassword;
    private Button mButtonSignIn;
    private Button mButtonSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = (EditText) view.findViewById(R.id.email);
        mPassword = (EditText) view.findViewById(R.id.password);

        mButtonSignIn = (Button) view.findViewById(R.id.btn_sign_in);
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().equals("") && mPassword.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Preencha os campos", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> params = new HashMap<>();

                    params.put(RequestManager.LOGIN, mEmail.getText().toString());
                    params.put(RequestManager.PASSWORD, mPassword.getText().toString());

                    Log.d(TAG, params.toString());

                    sendRequest(params);
                }
            }
        });



        mButtonSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new SignUpFirstPageFragment(), R.id.fragment_layout, true);
            }
        });
    }

    private void sendRequest(Map<String, String> params) {
        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "login", new NetworkRequestCallback<JSONObject>() {
            @Override
            public void onRequestResponse(JSONObject response) {
                HashMap<String, String> params = RequestManager.jsonToMap(response);
                String message = params.get(RequestManager.MESSAGE);

                if (message.equals(RequestManager.MESSAGES.PASSWORD_DONT_MATCH.name())) {
                    Toast.makeText(mContext, getResources().getText(R.string.password_incorrect), Toast.LENGTH_LONG).show();
                } else if (message.equals(RequestManager.MESSAGES.EMAIL_DOES_NOT_EXIST.name())) {
                    Toast.makeText(mContext, getResources().getText(R.string.email_not_exist), Toast.LENGTH_LONG).show();
                } else if (message.equals(RequestManager.MESSAGES.AUTHENTICATED.name())) {
                    SharedPrefsUtil.setUserLogin(mContext, mEmail.getText().toString());
                    SharedPrefsUtil.setUserPassword(mContext, mPassword.getText().toString());

                    User.getInstance().saveUserData(params);
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onRequestError(VolleyError error) {
                UnivoxerLog.d(TAG, error.toString());
            }
        });
    }
}