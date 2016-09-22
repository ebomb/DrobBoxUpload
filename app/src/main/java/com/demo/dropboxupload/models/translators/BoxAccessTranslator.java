package com.demo.dropboxupload.models.translators;

import com.demo.dropboxupload.models.BoxAccess;
import com.demo.dropboxupload.utils.JSONHelper;

import org.json.JSONObject;

/**
 * Created by Eli on 9/22/2016.
 * Translate Box Request to get Access Token
 */

public class BoxAccessTranslator {
    public static BoxAccess getBoxAccess(String response) {
        BoxAccess boxAccess = new BoxAccess();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boxAccess.setAccessToken(JSONHelper.getString(jsonObject, BoxAccess.Keys.ACCESS_TOKEN));
            boxAccess.setRefreshToken(JSONHelper.getString(jsonObject, BoxAccess.Keys.REFRESH_TOKEN));
            boxAccess.setErrorMessage(JSONHelper.getString(jsonObject, BoxAccess.Keys.ERROR_DESCRIPTION));
        } catch (Exception e) {
            e.printStackTrace();
            boxAccess.setErrorMessage(e.getMessage());
        }
        return boxAccess;
    }
}
