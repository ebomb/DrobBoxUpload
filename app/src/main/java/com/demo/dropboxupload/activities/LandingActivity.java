package com.demo.dropboxupload.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class LandingActivity extends BaseActivity {

    public static int UPLOAD_TYPE;
    FullAccount account;


    @Inject
    Context mContext;
    @Inject
    RequestQueue mRequestQueue;

    @BindView(R.id.box_button)
    FloatingActionButton fab1;
    @BindView(R.id.dropbox_button)
    FloatingActionButton fab2;

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
                String accessToken = getDropboxAccessToken();
                if (!TextUtils.isEmpty(accessToken)) {
                    DropboxClientFactory.init(accessToken);
                    startActivity(FilesActivity.getIntent(LandingActivity.this, "", UPLOAD_TYPE));
                }
                break;
            case UPLOAD_TYPE_BOX:
                break;
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
    public void onDropboxClick(@Nullable View view) {
        UPLOAD_TYPE = UPLOAD_TYPE_DROPBOX;
        if (!TextUtils.isEmpty(getDropboxAccessToken())) {
            // SUCCESSFUL AUTH
            startActivity(FilesActivity.getIntent(LandingActivity.this, "", UPLOAD_TYPE));
        } else {
            // START AUTH
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
