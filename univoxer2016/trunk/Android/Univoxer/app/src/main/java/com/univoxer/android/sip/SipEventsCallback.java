package com.univoxer.android.sip;

import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

/**
 * Created by Gustavo on 10/07/2016.
 * SipEventsCallback
 */
public interface SipEventsCallback {

    void onCallStatusChanged(String accountID, int callID, pjsip_inv_state callStateCode, long connectTimestamp, boolean isLocalHold, boolean isLocalMute);
    void onRegistration(String accountID, pjsip_status_code registrationStateCode);
    void onIncomingCall(String accountID, int callID, String displayName, String remoteUri);
    void onOutgoingCall(String accountID, int callID, String number);
}
