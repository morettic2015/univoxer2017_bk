package com.univoxer.android;

import android.app.Application;
import android.content.Intent;

import com.univoxer.android.network.NetworkManager;
import com.univoxer.android.sip.SipManagerService;

/**
 * Created by Gustavo on 26/05/2016.
 * UnivoxerApplication
 */
public class UnivoxerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getInstance().init(this);

        startService(new Intent(this, SipManagerService.class));
    }
}
