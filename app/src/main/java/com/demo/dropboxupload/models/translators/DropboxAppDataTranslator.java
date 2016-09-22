package com.demo.dropboxupload.models.translators;

import android.content.Context;

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
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            mDropboxAppData.setAppKey(JSONHelper.getString(obj, DropboxAppData.Keys.APP_KEY));
            mDropboxAppData.setSecretKey(JSONHelper.getString(obj, DropboxAppData.Keys.APP_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropboxAppData;
    }

    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("DropboxApp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
