package com.univoxer.android.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.univoxer.android.MainActivity;
import com.univoxer.android.R;
import com.univoxer.android.utils.UnivoxerLog;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.login.adapter.LanguageAdapter;
import com.univoxer.android.user.User;
import com.univoxer.android.network.NetworkManager;
import com.univoxer.android.network.NetworkRequestCallback;
import com.univoxer.android.network.RequestManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo on 29/06/2016.
 * SignUpFourthPageFragment
 */
public class SignUpFourthPageFragment extends BaseFragment {
    private static final String TAG = SignUpFourthPageFragment.class.getSimpleName();

    private ListView mListView;
    private Button mButton;
    private LanguageAdapter mAdapter;

    private ImageView mFlagImage;
    private TextView mLanguageText;
    private LanguageAdapter.FlagItem mFlagItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_second_page_fragment, container, false);

        mFlagImage = (ImageView) view.findViewById(R.id.flag_selected);
        mLanguageText = (TextView) view.findViewById(R.id.language_selected);

        mAdapter = new LanguageAdapter(mContext, LanguageAdapter.PROFICIENCY, true);

        mFlagImage.setImageResource(mAdapter.getItem(0).mIcon);
        mLanguageText.setText(mAdapter.getItem(0).mTitle);

        mListView = (ListView) view.findViewById(R.id.listview_language);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LanguageAdapter.FlagItem flagItem = mAdapter.getItem(position);
                mFlagImage.setImageResource(flagItem.mIcon);
                mLanguageText.setText(flagItem.mTitle);

                mFlagItem = mAdapter.getItem(position);
            }
        });

        mButton = (Button) view.findViewById(R.id.btn_next);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProficiency();
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (User.getInstance().getUserData(RequestManager.NATURE) != null) {
            mFlagImage.setImageResource(mAdapter.getItemByToken(User.getInstance().getUserData(RequestManager.NATURE)).mIcon);
            mLanguageText.setText(mAdapter.getItemByToken(User.getInstance().getUserData(RequestManager.NATURE)).mTitle);
        }
    }

    private void addProficiency() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.PROFICIENCY.name());
        params.put(RequestManager.ID_USER, User.getInstance().getUserData(RequestManager.ID_USER));
        params.put(RequestManager.OPT, RequestManager.OPT_ENUM.ADD.name());
        params.put(RequestManager.ID_LANG, String.valueOf(mFlagItem.mId));

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "addProficiency", new NetworkRequestCallback<JSONObject>() {
            @Override
            public void onRequestResponse(JSONObject response) {
                HashMap<String, String> params = RequestManager.jsonToMap(response);
                String message = params.get(RequestManager.MESSAGE);

                UnivoxerLog.d(TAG, "addProficiency: " + message);
            }

            @Override
            public void onRequestError(VolleyError error) {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}