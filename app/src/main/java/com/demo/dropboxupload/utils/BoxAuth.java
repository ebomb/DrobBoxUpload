package com.demo.dropboxupload.utils;

import android.widget.Toast;

import com.box.androidsdk.content.auth.BoxAuthentication;
import com.box.androidsdk.content.models.BoxSession;
import com.demo.dropboxupload.activities.LandingActivity;
import com.demo.dropboxupload.di.DemoApplication;

import javax.inject.Inject;

/**
 * Created by Eli on 9/22/2016.
 */

public class BoxAuth implements BoxAuthentication.AuthListener {

    @Inject BoxSession mBoxSession;

    private LandingActivity mParentActivity;
    private static BoxAuth instance;

    private BoxAuth(LandingActivity context) {
        this.mParentActivity = context;
    }

    public static BoxAuth createInstance(LandingActivity context) {
        if (instance == null)
            instance = new BoxAuth(context);
        return instance.editContext(context);
    }

    private BoxAuth editContext(LandingActivity parentActivity) {
        // Inject dependencies
        ((DemoApplication) parentActivity.getApplication()).component().inject(this);
        this.mParentActivity = parentActivity;
        return this;
    }

    // Begins the OAuth Process
    public void beginOAuth() {
        mBoxSession.setSessionAuthListener(this);
        mBoxSession.authenticate();
    }

    @Override
    public void onAuthCreated(BoxAuthentication.BoxAuthenticationInfo info) {
        // Start the Box upload process
        if (info != null) {
            mParentActivity.startFilesActivity(info.accessToken());
        }
    }

    @Override
    public void onAuthFailure(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        Toast.makeText(mParentActivity, "Auth Failure", Toast.LENGTH_LONG).show();
        beginOAuth();
    }

    @Override
    public void onLoggedOut(BoxAuthentication.BoxAuthenticationInfo info, Exception ex) {
        beginOAuth();
    }

    @Override
    public void onRefreshed(BoxAuthentication.BoxAuthenticationInfo info) {
        // Do nothing
    }
}
