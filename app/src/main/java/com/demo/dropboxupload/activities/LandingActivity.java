package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.models.DropboxAppData;
import com.demo.dropboxupload.models.translators.DropboxAppDataTranslator;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.BoxAuth;
import com.demo.dropboxupload.utils.Preferences;
import com.dropbox.core.android.Auth;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class LandingActivity extends AppCompatActivity {

    public static int UPLOAD_TYPE;
    private static final String KEY_DROPBOX_TOKEN = "KEY_DROPBOX_TOKEN";

    @Inject Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
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
     * Starts up the OAuth process
     */
    public void onBoxClick(View view) {
        UPLOAD_TYPE = UPLOAD_TYPE_BOX;
        BoxAuth.createInstance(LandingActivity.this).beginOAuth();
    }

    // Starts up the activity that lets you choose your Image file
    public void startFilesActivity(String accessToken) {
        String mFolderName = "0";

        // Initiate Dropbox client object if necessary
        if (UPLOAD_TYPE == UPLOAD_TYPE_DROPBOX) {
            DropboxClientFactory.init(accessToken);
            mFolderName = "";
        }

        // Start Files Activity
        startActivity(FilesActivity.getIntent(LandingActivity.this, mFolderName, UPLOAD_TYPE));
    }

    /* Checks if access token has been saved before. If not, request Auth and save access token
    *  This returns the saved access token
    */
    public String getDropboxAccessToken() {
        String accessToken = Preferences.getPreference(KEY_DROPBOX_TOKEN, mContext, "");
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                Preferences.setPreference(KEY_DROPBOX_TOKEN, accessToken, mContext);
            }
        }
        return accessToken;
    }
}
