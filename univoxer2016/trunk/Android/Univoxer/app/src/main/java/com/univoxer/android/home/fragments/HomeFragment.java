package com.univoxer.android.home.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.univoxer.android.R;
import com.univoxer.android.utils.SharedPrefsUtil;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.home.activity.HomeActivity;
import com.univoxer.android.login.adapter.LanguageAdapter;

/**
 * Created by Gustavo on 02/06/2016.
 * HomeFragment
 */
public class HomeFragment extends BaseFragment {

    private final static String TAG = HomeFragment.class.getSimpleName();

    private LanguageAdapter mAdapter;
    private Button mButtonCall;

    private ImageView mFlagUser;
    private ImageView mFlagTranslator;

    private TextView mFlagUserText;
    private TextView mFlagTranslatorText;

    private LinearLayout mUserLayout;
    private LinearLayout mTranslatorLayout;

    private int mLanguageUserId;
    private int mLanguageTranslatorId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mAdapter = new LanguageAdapter(mContext, LanguageAdapter.PROFICIENCY, true);
        LanguageAdapter.FlagItem flagItemUser = mAdapter.getItem(SharedPrefsUtil.getUserCallFlag(mContext)-1);
        LanguageAdapter.FlagItem flagItemTranslator = mAdapter.getItem(SharedPrefsUtil.getTranslatorCallFlag(mContext)-1);

        mButtonCall = (Button) view.findViewById(R.id.btn_call);

        mFlagUser = (ImageView) view.findViewById(R.id.flag_user);
        mFlagUserText = (TextView) view.findViewById(R.id.flag_user_text);
        mUserLayout = (LinearLayout) view.findViewById(R.id.user_layout);
        setUserFlag(flagItemUser);

        mFlagTranslator = (ImageView) view.findViewById(R.id.flag_translator);
        mFlagTranslatorText = (TextView) view.findViewById(R.id.flag_translator_text);
        mTranslatorLayout = (LinearLayout) view.findViewById(R.id.translator_layout);
        setTranslatorFlag(flagItemTranslator);

        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity activity = (HomeActivity) getActivity();
        activity.enableSideBar();
        activity.getCredits();
    }

    private void setListeners() {
        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageDialog(LanguageAdapter.NATURE);
            }
        });

        mTranslatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageDialog(LanguageAdapter.PROFICIENCY);
            }
        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity) getActivity();
                homeActivity.callTranslate(mLanguageUserId, mLanguageTranslatorId);
                SharedPrefsUtil.setUserCallFlag(mContext, mLanguageUserId);
                SharedPrefsUtil.setTranslatorCallFlag(mContext, mLanguageTranslatorId);
            }
        });
    }

    private void setLanguageDialog(final String type) {

        //mAdapter.removeItemByToken(User.getInstance().getUserData(RequestManager.NATURE));

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (type.equals(LanguageAdapter.NATURE)) {
            builder.setTitle(R.string.title_from_dialog);
        } else if (type.equals(LanguageAdapter.PROFICIENCY)) {
            builder.setTitle(R.string.title_to_dialog);
        }

        builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                LanguageAdapter.FlagItem flag = mAdapter.getItem(position);
                if (type.equals(LanguageAdapter.NATURE)) {
                    setUserFlag(flag);
                } else if (type.equals(LanguageAdapter.PROFICIENCY)) {
                    setTranslatorFlag(flag);
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show().getListView().setDividerHeight(20);
    }

    private void setUserFlag(LanguageAdapter.FlagItem flag) {
        mFlagUser.setImageResource(flag.mIcon);
        mFlagUserText.setText(flag.mTitle);
        mLanguageUserId = flag.mId;
    }

    private void setTranslatorFlag(LanguageAdapter.FlagItem flag) {
        mFlagTranslator.setImageResource(flag.mIcon);
        mFlagTranslatorText.setText(flag.mTitle);
        mLanguageTranslatorId = flag.mId;
    }
}
