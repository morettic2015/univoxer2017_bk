package com.univoxer.android.sip;

import java.util.HashMap;

/**
 * Created by Gustavo on 01/08/2016.
 * ResponseCallback
 */
public interface ResponseCallback {

    void onSuccess(HashMap<String, String> response);
    void onError(Exception error);
}
