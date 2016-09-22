package com.demo.dropboxupload.models.translators;

import android.content.Context;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.models.DropboxAppData;
import com.demo.dropboxupload.utils.JSONHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Eli on 9/20/2016.
 */

public class DropboxAppDataTranslator {

    public static DropboxAppData getDropboxAppDetails(Context context) {
        DropboxAppData mDropboxAppData = new DropboxAppData();
        try {
            JSONObject clientSecrets = JSONHelper.getJSONResource(context, R.raw.box_client_secrets);
            mDropboxAppData.setAppKey(JSONHelper.getString(clientSecrets, DropboxAppData.Keys.APP_KEY));
            mDropboxAppData.setSecretKey(JSONHelper.getString(clientSecrets, DropboxAppData.Keys.APP_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropboxAppData;
    }
}
