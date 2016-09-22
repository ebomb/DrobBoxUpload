package com.demo.dropboxupload.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demo.dropboxupload.R;
import com.demo.dropboxupload.interfaces.BoxAccessTokenCallback;
import com.demo.dropboxupload.models.BoxAccess;
import com.demo.dropboxupload.models.BoxAppData;
import com.demo.dropboxupload.models.translators.BoxAccessTranslator;
import com.demo.dropboxupload.net.AppStringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eli on 9/22/2016.
 */

public class BoxAPI {

    // API call to exchange auth code for access token
    public static void requestAccessToken(String authCode,
                                          BoxAppData boxAppData,
                                          final Context context,
                                          RequestQueue requestQueue,
                                          final BoxAccessTokenCallback boxAccessTokenCallback) {
        // Create our parameters
        Map<String, String> params = new HashMap<>();
        params.put(BoxAppData.Keys.GRANT_TYPE, BoxAppData.Keys.AUTHORIZATION_CODE);
        params.put(BoxAppData.Keys.CODE, authCode);
        params.put(BoxAppData.Keys.CLIENT_ID, boxAppData.getClientID());
        params.put(BoxAppData.Keys.CLIENT_SECRET, boxAppData.getClientSecret());
        params.put(BoxAppData.Keys.REDIRECT_URI, boxAppData.getRedirectURI());

        // Handle successful response from POST call
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Translate response to a model class
                BoxAccess boxAccess = BoxAccessTranslator.getBoxAccess(response);

                // Check for parsing error
                if (boxAccess == null) {
                    boxAccessTokenCallback.onError(context.getString(R.string.error_has_occurred));
                    return;
                }

                // Check for Box API error
                if (TextUtils.isEmpty(boxAccess.getErrorMessage())) {
                    boxAccessTokenCallback.onComplete(boxAccess);
                } else {
                    boxAccessTokenCallback.onError(boxAccess.getErrorMessage());
                }
            }
        };

        // Handle error response from POST call
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                boxAccessTokenCallback.onError(e.getMessage());
            }
        };

        // Create our request to get Access Token
        AppStringRequest mRequest = new AppStringRequest(
                Request.Method.POST,
                context,
                boxAppData.getTokenURI(),
                params,
                response,
                error);

        // Make API call
        requestQueue.add(mRequest);
    }
}
