package com.demo.dropboxupload.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.demo.dropboxupload.utils.Preferences;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Eli on 9/22/2016.
 * Custom Volley Request object class to customize our request to
 * change retry policy & save cookie
 */

public class AppStringRequest extends Request<String> {

    private Context mContext;
    private final Response.Listener<String> mListener;
    private Map<String, String> mParams, mHeader;
    private static final int TIMEOUT = 60 * 1000;

    public AppStringRequest(int method,
                            Context context,
                            String url,
                            Map<String, String> parameters,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mContext = context;
        this.mListener = listener;
        this.mParams = parameters;
        this.mHeader = null;
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public AppStringRequest(int method,
                            Context context,
                            String url,
                            Map<String, String> header,
                            Map<String, String> parameters,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mContext = context;
        this.mListener = listener;
        this.mParams = parameters;
        this.mHeader = header;
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeader != null) {
            return mHeader;
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        //Get our cookie and save it for future authentication
        Preferences.setCookie(response.headers, mContext);

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(response.data);
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        this.mListener.onResponse(response);
    }
}
