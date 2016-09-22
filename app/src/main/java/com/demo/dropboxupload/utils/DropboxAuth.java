package com.demo.dropboxupload.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.activities.LandingActivity;
import com.demo.dropboxupload.models.DropboxAppData;
import com.demo.dropboxupload.models.translators.DropboxAppDataTranslator;
import com.dropbox.core.android.Auth;

/**
 * Created by Eli on 9/22/2016.
 * Class that handles Auth operations for Dropbox.com
 */

public class DropboxAuth {

    private static final String KEY_DROPBOX_TOKEN = "KEY_DROPBOX_TOKEN";
    private static DropboxAuth instance;
    private LandingActivity mParentActivity;

    private DropboxAuth(LandingActivity context) {
        this.mParentActivity = context;
    }

    public static DropboxAuth createInstance(LandingActivity context) {
        if (instance == null)
            instance = new DropboxAuth(context);
        return instance.editContext(context);
    }

    private DropboxAuth editContext(LandingActivity parentActivity) {
        this.mParentActivity = parentActivity;
        return this;
    }

    // Check if user has given auth. If so, proceed to file picker. Else, start OAuth2
    public void checkOAuth() {
        String accessToken = getDropboxAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            mParentActivity.startFilesActivity(accessToken);
        } else {
            // START NEW AUTH
            DropboxAppData dropboxAppData = DropboxAppDataTranslator.getDropboxAppDetails(mParentActivity);    // Get App key
            if (dropboxAppData != null && !TextUtils.isEmpty(dropboxAppData.getAppKey())) {
                Auth.startOAuth2Authentication(mParentActivity, dropboxAppData.getAppKey());               // Begin Auth
            } else {
                Toast.makeText(mParentActivity, R.string.error_detecting_dropbox_app, Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Checks if access token has been saved before. If not, request Auth and save access token
     *  This returns the saved access token
     */
    public String getDropboxAccessToken() {
        String accessToken = Preferences.getPreference(KEY_DROPBOX_TOKEN, mParentActivity, "");
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                Preferences.setPreference(KEY_DROPBOX_TOKEN, accessToken, mParentActivity);
            }
        }
        return accessToken;
    }
}
