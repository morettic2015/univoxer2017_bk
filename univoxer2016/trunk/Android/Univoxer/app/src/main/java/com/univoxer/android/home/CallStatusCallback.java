package com.univoxer.android.home;

import org.pjsip.pjsua2.pjsip_inv_state;

/**
 * Created by Gustavo on 10/07/2016.
 * CallStatusCallback
 */
public interface CallStatusCallback {

    void onCallStatusChanged(pjsip_inv_state callStateCode);
}
