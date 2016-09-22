package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.demo.dropboxupload.R;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.models.BoxAppData;
import com.demo.dropboxupload.models.DropboxAppData;
import com.demo.dropboxupload.models.translators.BoxAppDataTranslator;
import com.demo.dropboxupload.models.translators.DropboxAppDataTranslator;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.AppConstants;
import com.demo.dropboxupload.utils.BoxAuth;
import com.demo.dropboxupload.utils.Preferences;
import com.dropbox.core.android.Auth;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class LandingActivity extends BaseActivity {

    public static int UPLOAD_TYPE;

    @Inject Context mContext;
    @Inject RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ((DemoApplication) getApplication()).component().inject(this);  // Inject dependencies
        ButterKnife.bind(this);     // Bind Views
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is coming from giving dropbox auth
        if (UPLOAD_TYPE == UPLOAD_TYPE_DROPBOX) {
            String accessToken = getDropboxAccessToken();
            if (!TextUtils.isEmpty(accessToken)) {
                startFilesActivity(accessToken);
            }
        }
    }

    /**
     * DROPBOX UPLOAD: Performs click action for Dropbox option.
     * Checks if user has given auth already. Once auth is given, launch file picker.
     */
    public void onDropboxClick(View view) {
        UPLOAD_TYPE = UPLOAD_TYPE_DROPBOX;

        // Check if user has given auth
        String accessToken = getDropboxAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            startFilesActivity(accessToken);
        } else {
            // START NEW AUTH
            DropboxAppData dropboxAppData = DropboxAppDataTranslator.getDropboxAppDetails(mContext);    // Get App key
            if (dropboxAppData != null && !TextUtils.isEmpty(dropboxAppData.getAppKey())) {
                Auth.startOAuth2Authentication(this, dropboxAppData.getAppKey());               // Begin Auth
            } else {
                Toast.makeText(mContext, R.string.error_detecting_dropbox_app, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * BOX UPLOAD: Performs click action for Box option.
     * Checks if user has given auth already. Once auth is given, launch file picker.
     */
    public void onBoxClick(View view) {
        UPLOAD_TYPE = UPLOAD_TYPE_BOX;

        // Begin OAuth Process
        BoxAuth.createInstance(LandingActivity.this).beginOAuth();

/*
        // Request Login and access to Box
        BoxOAuthDialog dialog = new BoxOAuthDialog(LandingActivity.this, mBoxAppData, new BoxOAuthDialog.BoxAuthCallback() {
            @Override
            public void onComplete(String authCode) {
                Log.e("AUTHCODE", authCode);

                // EXCHANGE AUTH CODE FOR ACCESS TOKEN
                BoxAPI.requestAccessToken(authCode, mBoxAppData, mContext, mRequestQueue, new BoxAccessTokenCallback() {
                    @Override
                    public void onComplete(BoxAccess boxAccess) {
                        // Store access token and refresh token
                        Preferences.setPreference(AppConstants.KEY_BOX_ACCESS_TOKEN, boxAccess.getAccessToken(), mContext);
                        Preferences.setPreference(AppConstants.KEY_BOX_REFRESH_TOKEN, boxAccess.getAccessToken(), mContext);

                        // Start the Box upload process
                        startFilesActivity("");
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showMessage(errorMessage);    // Error occurred while requesting access token
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                showMessage(errorMessage);    // Error occurred while requesting access to Box

            }
        });
        dialog.show();*/
    }

    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    public void startFilesActivity(String accessToken) {
        // Initiate Dropbox client object if necessary
        if (UPLOAD_TYPE == UPLOAD_TYPE_DROPBOX) {
            DropboxClientFactory.init(accessToken);
        }

        startActivity(FilesActivity.getIntent(LandingActivity.this, "0", UPLOAD_TYPE));
    }

    public String getDropboxAccessToken() {
        String accessToken = Preferences.getPreference(AppConstants.KEY_DROPBOX_TOKEN, mContext, "");
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                Preferences.setPreference(AppConstants.KEY_DROPBOX_TOKEN, accessToken, mContext);
            }
        }
        return accessToken;
    }
}
