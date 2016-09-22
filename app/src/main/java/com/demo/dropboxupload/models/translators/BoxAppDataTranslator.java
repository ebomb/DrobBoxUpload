package com.demo.dropboxupload.models.translators;

import android.content.Context;
import android.util.Log;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.models.BoxAppData;
import com.demo.dropboxupload.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by Eli on 9/22/2016.
 * Tool that parses the box developer app details
 * to get client_id, secrect_id, etc.
 */

public class BoxAppDataTranslator {

    public static BoxAppData getBoxAppData(Context mContext) {
        BoxAppData boxAppData = new BoxAppData();
        try {
            JSONObject clientSecrets = JSONHelper.getJSONResource(mContext, R.raw.box_client_secrets);
            boxAppData.setAuthURI(JSONHelper.getString(clientSecrets, BoxAppData.Keys.AUTH_URI));
            boxAppData.setClientID(JSONHelper.getString(clientSecrets, BoxAppData.Keys.CLIENT_ID));
            boxAppData.setClientSecret(JSONHelper.getString(clientSecrets, BoxAppData.Keys.CLIENT_SECRET));
            boxAppData.setTokenURI(JSONHelper.getString(clientSecrets, BoxAppData.Keys.TOKEN_URI));
            boxAppData.setRedirectURI(JSONHelper.getString(clientSecrets, BoxAppData.Keys.REDIRECT_URI));
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("Parsing Error", "In BoxAppData");
        }
        return boxAppData;
    }
}
