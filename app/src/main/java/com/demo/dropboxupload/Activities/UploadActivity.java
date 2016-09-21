package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.dropbox.core.DbxException;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadActivity extends BaseActivity {

    FullAccount account;

    @Inject Context mContext;
    @Inject RequestQueue mRequestQueue;

    @BindView(R.id.box_button) FloatingActionButton fab1;
    @BindView(R.id.dropbox_button) FloatingActionButton fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ((DemoApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String accessToken = getDropboxAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            Toast.makeText(mContext, "AUTH SUCCESS!", Toast.LENGTH_LONG).show();
            DropboxClientFactory.init(accessToken);

            // Get current account info
            new GetDropboxAccount().execute(DropboxClientFactory.getClient());
        }
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

    /**
     * Performs click action for Dropbox option.
     * Checks if user has given auth already. Once auth is given, launch file picker.
     */
    public void onDropboxClick(View view) {
        if (!TextUtils.isEmpty(getDropboxAccessToken())) {
            // Auth was given already, we now open the Photo Library to choose file
            startActivity(FilePickerActivity.getIntent(UploadActivity.this, ""));
        } else {
            // Begin auth process through Dropbox
            DropboxApp dropboxApp = DropboxAppTranslator.getDropboxAppDetails(mContext);    // Get App key
            if (dropboxApp != null && !TextUtils.isEmpty(dropboxApp.getAppKey())) {
                Auth.startOAuth2Authentication(this, dropboxApp.getAppKey());               // Begin Auth
            } else {
                Toast.makeText(mContext, R.string.error_detecting_dropbox_app, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetDropboxAccount extends AsyncTask<DbxClientV2, Void, String> {

        @Override
        protected String doInBackground(DbxClientV2... client) {
            try {
                if (client != null && client[0] != null) {
                    account = client[0].users().getCurrentAccount();
                }
            } catch (DbxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String out) {
            if (account != null) {
                Log.e("Account Info", account.getName().getDisplayName());
            } else {
                Log.e("Account Info", "NULL");
            }
        }
    }
}
