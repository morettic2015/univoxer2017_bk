package com.univoxer.android.home.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.univoxer.android.R;
import com.univoxer.android.utils.Util;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.home.activity.HomeActivity;
import com.univoxer.android.network.RequestManager;
import com.univoxer.android.user.User;

/**
 * Created by Gustavo on 10/07/2016.
 * OutgoingFragment
 */
public class OutgoingFragment extends BaseFragment {

    private static final String TAG = OutgoingFragment.class.getSimpleName();

    private AudioManager mAudioManager;
    public static final String TRANSLATE_NAME = "TRANSLATE_NAME";
    public static final String FLAG_NATURE = "FLAG_NATURE";
    public static final String FLAG_PROFICIENCY = "FLAG_PROFICIENCY";

    private ImageView mCloseCall;
    private ImageView mAudioType;
    private ImageView mNatureFlag;
    private ImageView mProficiencyFlag;
    private TextView mCredits;
    private TextView mStatusCall;

    private CountDownTimer mCountDownTimer;

    private int mNatureFlagId;
    private int mProficiencyFlagId;
    private String mTranslatorName;

    private HomeActivity mHomeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mNatureFlagId = args.getInt(FLAG_NATURE);
        mProficiencyFlagId = args.getInt(FLAG_PROFICIENCY);
        mTranslatorName = args.getString(TRANSLATE_NAME);

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setSpeakerphoneOn(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity activity = (HomeActivity) getActivity();
        activity.disableSideBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.outgoing_fragment, container, false);

        TextView translator = (TextView) view.findViewById(R.id.translate_name);
        if (mTranslatorName != null) {
            translator.setText(mTranslatorName);
        }

        long creditsTime = Long.parseLong(User.getInstance().getUserData(RequestManager.CREDITS));

        mStatusCall = (TextView) view.findViewById(R.id.status_call);
        mStatusCall.setText(R.string.call_status_calling);

        mCredits = (TextView) view.findViewById(R.id.credits);
        mCredits.setText(Util.formatTime(creditsTime));

        mCountDownTimer = new CountDownTimer(creditsTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mCredits.setText(Util.formatTime(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mHomeActivity = (HomeActivity) getActivity();
                mHomeActivity.closeCall();
            }
        };

        mNatureFlag = (ImageView) view.findViewById(R.id.nature_flag);
        mNatureFlag.setImageResource(Util.getFlagResource(mNatureFlagId));

        mProficiencyFlag = (ImageView) view.findViewById(R.id.proficiency_flag);
        mProficiencyFlag.setImageResource(Util.getFlagResource(mProficiencyFlagId));

        mCloseCall = (ImageView) view.findViewById(R.id.close_call);
        mAudioType = (ImageView) view.findViewById(R.id.audio_type);

        mCloseCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeActivity = (HomeActivity) getActivity();
                mHomeActivity.closeCall();
            }
        });

        mAudioType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioManager.setSpeakerphoneOn(!mAudioManager.isSpeakerphoneOn());
                if (mAudioManager.isSpeakerphoneOn()) {
                    mAudioType.setImageResource(R.drawable.audio_type);
                } else {
                    mAudioType.setImageResource(R.drawable.audio_type_off);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void startTimer() {
        mStatusCall.setText(R.string.call_status_connected);
        mCountDownTimer.start();
    }

    public void finishTimer() {
        mCountDownTimer.cancel();
    }
}

