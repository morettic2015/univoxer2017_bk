package com.univoxer.android.home.activity;

import android.content.ComponentName;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.univoxer.android.MainActivity;
import com.univoxer.android.R;
import com.univoxer.android.user.UserActivity;
import com.univoxer.android.utils.SharedPrefsUtil;
import com.univoxer.android.utils.UnivoxerLog;
import com.univoxer.android.commons.BaseActivity;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.home.NavItem;
import com.univoxer.android.home.adapter.DrawerListAdapter;
import com.univoxer.android.home.fragments.OutgoingFragment;
import com.univoxer.android.home.fragments.HomeFragment;
import com.univoxer.android.network.NetworkManager;
import com.univoxer.android.network.NetworkRequestCallback;
import com.univoxer.android.settings.SettingsActivity;
import com.univoxer.android.sip.ResponseCallback;
import com.univoxer.android.sip.SipEventsCallback;
import com.univoxer.android.sip.SipManagerService;
import com.univoxer.android.user.User;
import com.univoxer.android.network.RequestManager;

import org.json.JSONObject;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo on 02/06/2016.
 * HomeActivity
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems = new ArrayList<>();

    private SipManagerService mSipManager;

    private TextView mCredits;

    private boolean mBounded;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder IBinder) {
            UnivoxerLog.d(SipManagerService.TAG, "onServiceConnected");
            mBounded = true;
            SipManagerService.LocalBinder mLocalBinder = (SipManagerService.LocalBinder) IBinder;
            mSipManager = mLocalBinder.getServerInstance(getActivity(), new SipEventsCallback() {
                @Override
                public void onCallStatusChanged(String accountID, int callID, pjsip_inv_state callStateCode, long connectTimestamp, boolean isLocalHold, boolean isLocalMute) {
                    if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
                        if (fragment instanceof OutgoingFragment) {
                            OutgoingFragment outgoingFragment = (OutgoingFragment) fragment;
                            if (outgoingFragment.isVisible()) {
                                outgoingFragment.startTimer();
                            }
                        }
                    }

                    if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                        switchFragment(new HomeFragment(), R.id.fragment_layout, false);

                        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
                        if (fragment instanceof OutgoingFragment) {
                            OutgoingFragment outgoingFragment = (OutgoingFragment) fragment;
                            if (outgoingFragment.isVisible()) {
                                outgoingFragment.finishTimer();
                            }
                        }
                    }
                }

                @Override
                public void onRegistration(String accountID, pjsip_status_code registrationStateCode) {

                }

                @Override
                public void onIncomingCall(String accountID, int callID, String displayName, String remoteUri) {
                }

                @Override
                public void onOutgoingCall(String accountID, int callID, String number) {

                }
            });

            if (mSipManager != null) {
                mSipManager.activityInFront(true);
            }

            mSipManager.registerOnSip(User.getInstance().getUserData(RequestManager.SIP_USER),
                    User.getInstance().getUserData(RequestManager.SIP_PASS),
                    User.getInstance().getUserData(RequestManager.SERVER_NAME_LOGIN));

            mSipManager.setStatusOnServerOnline(new ResponseCallback() {
                @Override
                public void onSuccess(HashMap<String, String> response) {
                    Log.d(TAG, response.get(RequestManager.MESSAGE));
                    UnivoxerLog.d(SipManagerService.TAG, "setStatusOnServerOnline :: onSuccess");
                }

                @Override
                public void onError(Exception error) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            UnivoxerLog.d(SipManagerService.TAG, "onServiceDisconnected");
            mSipManager = null;
            mBounded = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setSideMenu();
        addFragment(new HomeFragment(), R.id.fragment_layout, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCredits();

        UnivoxerLog.d(SipManagerService.TAG, "HomeActivity :: Binding Service");
        bindService(new Intent(this, SipManagerService.class), mConnection, 0);

        if (mSipManager != null) {
            mSipManager.activityInFront(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mSipManager != null) {
            mSipManager.activityInFront(false);
        }

        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void callTranslate(int userId, final int proficiencyId) {
        mSipManager.getTranslateProfile(userId, proficiencyId, new ResponseCallback() {
            @Override
            public void onSuccess(HashMap<String, String> response) {
                String translatorName = response.get(RequestManager.TRANSLATOR_NAME);

                Bundle bundle = new Bundle();
                bundle.putString(OutgoingFragment.TRANSLATE_NAME, translatorName);
                bundle.putInt(OutgoingFragment.FLAG_NATURE, Integer.parseInt(User.getInstance().getUserData(RequestManager.NATURE)));
                bundle.putInt(OutgoingFragment.FLAG_PROFICIENCY, proficiencyId);

                mSipManager.makeCall();
                goToCallFragment(new OutgoingFragment(), bundle);
            }

            @Override
            public void onError(Exception error) {

            }
        });
    }

    private HomeActivity getActivity() {
        return this;
    }

    public void closeCall() {
        mSipManager.closeCall();
    }

    public void acceptCall(String accountId, int callId) {
        mSipManager.acceptCall(accountId, callId);
    }

    private void goToCallFragment(Fragment fragment, Bundle bundle) {
        switchFragment(fragment, R.id.fragment_layout, bundle, false);
    }

    private void setSideMenu() {

        mNavItems.add(new NavItem(NavItem.PREFERENCE, "Preferences", "Change your preferences", R.drawable.gear));
        mNavItems.add(new NavItem(NavItem.PROFILE, "Profile", "Change your profile infos", R.drawable.man));
        mNavItems.add(new NavItem(NavItem.LOGOUT, "Logout", "Logout", R.drawable.ic_logout)); //ver outro icone

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView name = (TextView) mDrawerLayout.findViewById(R.id.userName);
        mCredits = (TextView) mDrawerLayout.findViewById(R.id.credits);
        TextView statusTranslator = (TextView) mDrawerLayout.findViewById(R.id.status_translator);

        name.setText(User.getInstance().getUserData(RequestManager.NAME));
        mCredits.setText(String.format(getResources().getString(R.string.credits_text), User.getInstance().getUserData(RequestManager.CREDITS)));

        if (User.getInstance().getUserData(RequestManager.ROLE) == null) {
            statusTranslator.setVisibility(View.GONE);
        } else {
            if (User.getInstance().isUser()) {
                statusTranslator.setVisibility(View.GONE);
            } else {
                String status;
                if (SharedPrefsUtil.getTranslatorStatus(mContext)) {
                    status = getResources().getString(R.string.online);
                } else {
                    status = getResources().getString(R.string.offline);
                }
                statusTranslator.setText(String.format(getResources().getString(R.string.status_translator_home), status));
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case NavItem.LOGOUT:
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        mSipManager.logout();
                        User.getInstance().clearUserData(mContext);
                        break;

                    case NavItem.PROFILE:
                        intent = new Intent(mContext, UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;

                    case NavItem.PREFERENCE:
                        intent = new Intent(mContext, SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                }
                mDrawerLayout.closeDrawer(mDrawerPane);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        if (fragment instanceof HomeFragment) {
            finish();
        }
    }

    public void getCredits() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestManager.ACTION, RequestManager.ACTIONS.MY_CREDITS.name());
        params.put(RequestManager.ID, User.getInstance().getUserData(RequestManager.ID_USER));

        NetworkManager.getInstance().doPostWithParams(RequestManager.URL, params, "get credits",
                new NetworkRequestCallback<JSONObject>() {
                    @Override
                    public void onRequestResponse(JSONObject response) {
                        HashMap<String, String> params = RequestManager.jsonToMap(response);
                        String credits = params.get(RequestManager.CREDITS);
                        if (credits == null) {
                            Toast.makeText(mContext, R.string.get_credits_error, Toast.LENGTH_LONG).show();
                            return;
                        }
                        User.getInstance().setUserData(RequestManager.CREDITS, credits);
                        if (mCredits != null && !credits.equals("null")) {
                            mCredits.setText(String.format(getResources().getString(R.string.credits_text), credits));
                        }
                    }

                    @Override
                    public void onRequestError(VolleyError error) {
                        Toast.makeText(mContext, R.string.get_credits_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void enableSideBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void disableSideBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
