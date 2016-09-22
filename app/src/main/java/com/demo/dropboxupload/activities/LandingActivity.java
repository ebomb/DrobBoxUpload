package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.BoxAuth;
import com.demo.dropboxupload.utils.DropboxAuth;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class LandingActivity extends AppCompatActivity {

    @Inject Context mContext;

    public static int UPLOAD_TYPE;
    DropboxAuth mDropboxAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ((DemoApplication) getApplication()).component().inject(this);  // Inject dependencies
        ButterKnife.bind(this);     // Bind Views
        mDropboxAuth = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is coming from giving dropbox auth
        if (mDropboxAuth != null && UPLOAD_TYPE == UPLOAD_TYPE_DROPBOX) {
            String accessToken = mDropboxAuth.getDropboxAccessToken();
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
        mDropboxAuth = DropboxAuth.createInstance(LandingActivity.this);
        mDropboxAuth.checkOAuth();
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
        String mFolderName = "0";   // Needs to be "0" folder name for Box

        // Initiate Dropbox client object if necessary
        if (UPLOAD_TYPE == UPLOAD_TYPE_DROPBOX) {
            DropboxClientFactory.init(accessToken);
            mFolderName = "";   // Needs to be empty folder name for Dropbox
        }

        // Start Files Activity
        startActivity(FilesActivity.getIntent(LandingActivity.this, mFolderName, UPLOAD_TYPE));
    }
}
