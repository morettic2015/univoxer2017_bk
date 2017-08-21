package com.univoxer.android.sip;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.univoxer.android.R;
import com.univoxer.android.utils.GeneralConstants;
import com.univoxer.android.utils.SharedPrefsUtil;
import com.univoxer.android.utils.UnivoxerLog;
import com.univoxer.android.utils.Util;
import com.univoxer.android.commons.BaseActivity;
import com.univoxer.android.network.NetworkManager;
import com.univoxer.android.network.NetworkRequestCallback;
import com.univoxer.android.network.RequestManager;
import com.univoxer.android.user.User;

import net.gotev.sipservice.BroadcastEventReceiver;
import net.gotev.sipservice.SipAccountData;
import net.gotev.sipservice.SipServiceCommand;

import org.json.JSONObject;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo on 01/08/2016.
 * SipManagerService
 */
public class SipManagerService extends Service {

    public static final String TAG = SipManagerService.class.getSimpleName();

    private SipAccountData mSipAccount = null;

    private boolean mSipRegistered = false;
    private boolean mStackStarted = false;
    private String SipAccountUriId = null;

    private String mCallToken = null;
    private String mSipAccountTranslate = null;

    private Context mContext;

    private SipEventsCallback mSipEventsCallback;

    private Notification.Builder mNotification;
    private NotificationManager mNotificationManager;

    private MediaPlayer mMediaPlayer;

    private boolean mCallAnswered = false;
    private boolean mLoggedOnSip = false;

    private boolean mActivityInFront = false;

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        UnivoxerLog.d(TAG, "onBind");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public SipManagerService getServerInstance(BaseActivity context, SipEventsCallback callback) {
            mSipEventsCallback = callback;
            mContext = context;
            UnivoxerLog.d(TAG, "getServerInstance :: instance of: " + mContext.getClass().getSimpleName());
            return SipManagerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UnivoxerLog.d(TAG, "onCreate");
        mSipEvents.register(this);

//        mNotification = new Notification.Builder(this);
//
//        Notification notif = mNotification.setContentTitle("Univoxer")
//                .setSmallIcon(R.drawable.notification_template_icon_bg)
//                .setOngoing(true)
//                .build();
//
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, notif);
    }

    public void activityInFront(boolean inFront) {
        mActivityInFront = inFront;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UnivoxerLog.d(TAG, "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        UnivoxerLog.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UnivoxerLog.d(TAG, "onDestroy");

        setStatusOnServerExit(new ResponseCallback() {
            @Override
            public void onSuccess(HashMap<String, String> response) {

            }

            @Override
            public void onError(Exception error) {

            }
        });

        if (mSipAccount != null) {
            SipServiceCommand.removeAccount(this, mSipAccount.getIdUri());
        }
        mSipEvents.unregister(this);
//        mNotificationManager.cancel(1);
    }

    public void setCallToken(String token) {
        mCallToken = token;
    }

    public void registerOnSip(String sipUser, String sipPass, String serverAddress) {
        UnivoxerLog.d(TAG, "setStatusOnServerOnline :: " + sipUser + " : " + sipPass + " : " + serverAddress);

        mSipAccount = new SipAccountData();
        mSipAccount.setHost(serverAddress)
                .setRealm(GeneralConstants.SIP_REALM)
                .setUsername(sipUser)
                .setPassword(sipPass);

        SipAccountUriId = SipServiceCommand.setAccount(this, mSipAccount);
        SipServiceCommand.getCodecPriorities(this);
        mSipRegistered = true;

        UnivoxerLog.d(TAG, "SipRegistered");

        if (!SharedPrefsUtil.getTranslatorStatus(this)) {
            setStatusOnServerOffline(new ResponseCallback() {
                @Override
                public void onSuccess(HashMap<String, String> response) {

                }

                @Override
                public void onError(Exception error) {

                }
            });
        }
    }

    public void makeCall() {
        SipServiceCommand.makeCall(this, mSipAccount.getIdUri(), mSipAccountTranslate);
    }

    public void acceptCall(String accountId, int callId) {
        SipServiceCommand.acceptIncomingCall(mContext, accountId, callId);
    }

    public void closeCall() {
        UnivoxerLog.d(TAG, "close Call");
        if (mSipAccount.getIdUri() == null) {
            SipServiceCommand.hangUpActiveCalls(this, SipAccountUriId);
        } else {
            SipServiceCommand.hangUpActiveCalls(this, mSipAccount.getIdUri());
        }
    }

    public void sendCallStatusAnswered() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.CALL_STATUS.name());
        params.put(RequestManager.TOKEN, mCallToken);
        params.put(RequestManager.STATUS_CALL, RequestManager.STATUS_ANSWERED);

        UnivoxerLog.d(TAG, "Status Answered");

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "call_status",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        UnivoxerLog.d(TAG, "Status Answered response");
                    }

                    @Override
                    public void onRequestError(VolleyError error) {

                    }
                });
    }

    public void sendCallStatusNotAnswered() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.CALL_STATUS.name());
        params.put(RequestManager.TOKEN, mCallToken);
        params.put(RequestManager.STATUS_CALL, RequestManager.STATUS_NOT_ANSWERED);

        UnivoxerLog.d(TAG, "Status Not Answered");

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "call_status",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        UnivoxerLog.d(TAG, "Status Not Answered response");
                    }

                    @Override
                    public void onRequestError(VolleyError error) {

                    }
                });
    }

    public void sendFinishCall() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.FINISH_PROFILE.name());
        params.put(RequestManager.TOKEN, mCallToken);

        UnivoxerLog.d(TAG, "Status Finish");

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "finish",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        UnivoxerLog.d(TAG, "Status Finish response");
                    }

                    @Override
                    public void onRequestError(VolleyError error) {

                    }
                });
    }

    private void setStatusOnServerOffline(final ResponseCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.STATUS.name());
        params.put(RequestManager.ID_USER, User.getInstance().getUserData(RequestManager.ID_USER));
        params.put(RequestManager.ONLINE, RequestManager.STATUS.OFF.name());

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "offline",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        HashMap<String, String> params = RequestManager.jsonToMap(response);
                        callback.onSuccess(params);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        callback.onError(error);
                    }
                });
    }

    private void setStatusOnServerExit(final ResponseCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.STATUS.name());
        params.put(RequestManager.ID_USER, User.getInstance().getUserData(RequestManager.ID_USER));
        params.put(RequestManager.ONLINE, RequestManager.STATUS.EXIT.name());

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "exit",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        HashMap<String, String> params = RequestManager.jsonToMap(response);
                        callback.onSuccess(params);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        callback.onError(error);
                    }
                });
    }

    public void setStatusOnServerOnline(final ResponseCallback callback) {
        if (mSipRegistered) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.STATUS.name());
        params.put(RequestManager.ID_USER, User.getInstance().getUserData(RequestManager.ID_USER));
        params.put(RequestManager.ONLINE, RequestManager.STATUS.ON.name());

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "online",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        HashMap<String, String> params = RequestManager.jsonToMap(response);

                        callback.onSuccess(params);

                        String sipUser = params.get(RequestManager.SIP_USER);
                        String sipPass = params.get(RequestManager.SIP_PASS);
                        String sipHost = params.get(RequestManager.SERVER_NAME);

                        registerOnSip(sipUser, sipPass, sipHost);
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        callback.onError(error);
                    }
                }

        );
    }

    public void logout() {
        SipServiceCommand.removeAccount(this, mSipAccount.getIdUri());
        setStatusOnServerExit(new ResponseCallback() {
            @Override
            public void onSuccess(HashMap<String, String> response) {

            }

            @Override
            public void onError(Exception error) {
            }
        });

        mSipRegistered = false;
    }


    public void getTranslateProfile(final int userId, final int proficiencyId, final ResponseCallback callback) {
        if (mSipAccount == null) {
            return;
        }

        if (!mLoggedOnSip) {
            return;
        }

        Map<String, String> params = new HashMap<>();

        params.put(RequestManager.NATURE, String.valueOf(userId));
        params.put(RequestManager.PROFICIENCY, String.valueOf(proficiencyId));
        params.put(RequestManager.ID_USER, User.getInstance().getUserData(RequestManager.ID_USER));
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.CALL_PROFILE.name());

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "call_translate",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        HashMap<String, String> params = RequestManager.jsonToMap(response);
                        String message = params.get(RequestManager.MESSAGE);
                        mSipAccountTranslate = params.get(RequestManager.SIP_USER_TRANSLATE);

                        if (message.equals(RequestManager.MESSAGES.TRANSLATOR_UNAVALIABLE.name()) || mSipAccountTranslate == null) {
                            Util.showAlert(getResources().getString(R.string.translator_unavailable), mContext);
                            return;
                        }

                        if (!mSipAccountTranslate.equals("null")) {
                            callback.onSuccess(params);
                            mCallToken = params.get(RequestManager.CALL_TOKEN);
                            UnivoxerLog.d(TAG, "SIP TRANSLATOR :: " + mSipAccountTranslate);
                        }
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        callback.onError(error);
                    }
                });
    }

    private BroadcastEventReceiver mSipEvents = new BroadcastEventReceiver() {
        @Override
        public void onRegistration(String accountID, pjsip_status_code registrationStateCode) {
            super.onRegistration(accountID, registrationStateCode);

            UnivoxerLog.d(TAG, registrationStateCode.toString());

            if (pjsip_status_code.PJSIP_SC_OK == registrationStateCode) {
                mLoggedOnSip = true;
                if (mActivityInFront) {
                    Toast.makeText(SipManagerService.this, R.string.authenticated, Toast.LENGTH_SHORT).show();
                }
            }

            if (mSipEventsCallback != null) {
                mSipEventsCallback.onRegistration(accountID, registrationStateCode);
            }
        }

        @Override
        public void onStackStatus(boolean started) {
            super.onStackStatus(started);
        }

        @Override
        public void onOutgoingCall(String accountID, int callID, String number) {
            super.onOutgoingCall(accountID, callID, number);
            if (mSipEventsCallback != null) {
                mSipEventsCallback.onOutgoingCall(accountID, callID, number);
            }
        }

        @Override
        public void onIncomingCall(String accountID, int callID, String displayName, String remoteUri) {
            super.onIncomingCall(accountID, callID, displayName, remoteUri);
            UnivoxerLog.d(TAG, "INCOMING CALL");

            Bundle bundle = new Bundle();
            bundle.putString(IncomingActivity.ACCOUNT_ID, accountID);
            bundle.putInt(IncomingActivity.CALL_ID, callID);
            bundle.putString(IncomingActivity.SIP_NUMBER, displayName);

            Intent intent = new Intent(getApplicationContext(), IncomingActivity.class);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mSipEventsCallback != null) {
                mSipEventsCallback.onIncomingCall(accountID, callID, displayName, remoteUri);
            }
        }

        @Override
        public void onCallState(String accountID, int callID, pjsip_inv_state callStateCode, long connectTimestamp, boolean isLocalHold, boolean isLocalMute) {
            super.onCallState(accountID, callID, callStateCode, connectTimestamp, isLocalHold, isLocalMute);
            UnivoxerLog.d(TAG, callStateCode.toString());

            if (mSipEventsCallback != null) {
                mSipEventsCallback.onCallStatusChanged(accountID, callID, callStateCode, connectTimestamp, isLocalHold, isLocalMute);
            }

            if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                mMediaPlayer = MediaPlayer.create(mContext, R.raw.ringtone);
                mMediaPlayer.start();
            }

            if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
                mCallAnswered = true;
                sendCallStatusAnswered();
                UnivoxerLog.d(TAG, "STATE CONNECTING");
            }

            if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                }

                UnivoxerLog.d(TAG, "STATE CONFIRMED");
            }

            if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                }

                if (mCallToken == null) {
                    return;
                }

                UnivoxerLog.d(TAG, "STATE DISCONNECTED");

                if (mCallAnswered) {
                    sendFinishCall();
                } else {
                    sendCallStatusNotAnswered();
                }

                mCallToken = null;
            }
        }
    };
}