package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.demo.dropboxupload.R;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.models.DropboxApp;
import com.demo.dropboxupload.models.translators.DropboxAppTranslator;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.AppConstants;
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
        switch (UPLOAD_TYPE) {
            case UPLOAD_TYPE_DROPBOX:
                // Check if user has given auth
                String accessToken = getDropboxAccessToken();
                if (!TextUtils.isEmpty(accessToken)) {
                    startFilesActivity(accessToken);
                }
                break;
            case UPLOAD_TYPE_BOX:

                break;
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
            DropboxApp dropboxApp = DropboxAppTranslator.getDropboxAppDetails(mContext);    // Get App key
            if (dropboxApp != null && !TextUtils.isEmpty(dropboxApp.getAppKey())) {
                Auth.startOAuth2Authentication(this, dropboxApp.getAppKey());               // Begin Auth
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


    }

    private void startFilesActivity(String accessToken) {
        // Initiate Dropbox client object
        DropboxClientFactory.init(accessToken);
        startActivity(FilesActivity.getIntent(LandingActivity.this, "", UPLOAD_TYPE));
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
