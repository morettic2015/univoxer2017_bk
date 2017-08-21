package com.univoxer.android.network;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by Gustavo on 26/05/2016.
 */
public class NetworkManager {

    private static NetworkManager mInstance;
    private Context mApplicationContext;
    public RequestQueue mRequestQueue;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if(mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public void init(Application application) {
        if(mApplicationContext == null) {
            mApplicationContext = application.getApplicationContext();
            mRequestQueue = Volley.newRequestQueue(mApplicationContext);
        }
    }

    public void cancelRequestsByTag(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public void cancelAllRequests() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void doGet(String url, Object tag,
                      NetworkRequestCallback<JSONObject> networkRequestCallback) {
        JsonObjectRequest jsonObjectRequest = buildJSONRequest(
                Request.Method.GET,
                url,
                null,
                tag,
                networkRequestCallback);
        doRequest(jsonObjectRequest);
    }

    public void doGetWithParams(String url, Map<String, String> params, Object tag,
                      NetworkRequestCallback<JSONObject> networkRequestCallback) {
        JsonObjectRequest jsonObjectRequest = buildJSONRequest(
                Request.Method.GET,
                RequestManager.buildUrlWithParams(url, params),
                null,
                tag,
                networkRequestCallback);
        doRequest(jsonObjectRequest);
    }

    public void doGetArray(String url, Object tag,
                           NetworkRequestCallback<JSONArray> networkRequestCallback) {
        // The Method is supposed to be GET
        JsonArrayRequest jsonArrayRequest = buildJSONRequest(
                url,
                tag,
                networkRequestCallback);
        doRequest(jsonArrayRequest);
    }

    public void doPost(String url, JSONObject jsonObject, Object tag,
                       NetworkRequestCallback<JSONObject> networkRequestCallback) {
        JsonObjectRequest jsonObjectRequest = buildJSONRequest(
                Request.Method.POST,
                url,
                jsonObject,
                tag,
                networkRequestCallback);
        doRequest(jsonObjectRequest);
    }

    public void doPostWithParams(String url, Map<String, String> params, Object tag,
                       NetworkRequestCallback<JSONObject> networkRequestCallback) {
        JsonObjectRequest jsonObjectRequest = buildJSONRequest(
                Request.Method.POST,
                RequestManager.buildUrlWithParams(url, params),
                null,
                tag,
                networkRequestCallback);
        doRequest(jsonObjectRequest);
    }

    private JsonObjectRequest buildJSONRequest(int method, String url, JSONObject jsonObject, Object tag,
                                               final NetworkRequestCallback<JSONObject> networkRequestCallback) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                notifyOnResponse(response, networkRequestCallback);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notifyOnErrorResponse(error, networkRequestCallback);
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, listener, errorListener);
        request.setTag(tag);
        return request;
    }

    private JsonArrayRequest buildJSONRequest(String url, Object tag,
                                              final NetworkRequestCallback<JSONArray> networkRequestCallback) {
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                notifyOnResponse(response, networkRequestCallback);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notifyOnErrorResponse(error, networkRequestCallback);
            }
        };

        // The Method is supposed to be GET
        JsonArrayRequest request = new JsonArrayRequest(url, listener, errorListener);
        request.setTag(tag);
        return request;
    }

    private <T> void doRequest(Request<T> request) {
        mRequestQueue.add(request);
    }

    private <T> void notifyOnResponse(T response,
                                      NetworkRequestCallback<T> networkRequestCallback) {
        if(networkRequestCallback != null) {
            networkRequestCallback.onRequestResponse(response);
        }
    }

    private <T> void notifyOnErrorResponse(VolleyError error,
                                           NetworkRequestCallback<T> networkRequestCallback) {
        if(networkRequestCallback != null) {
            networkRequestCallback.onRequestError(error);
        }
    }
}
