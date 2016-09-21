package com.demo.dropboxupload.activities;

/**
 * Created by Eli on 9/21/2016.
 */

import android.app.Activity;
import android.os.Bundle;

import com.demo.dropboxupload.di.DemoApplication;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Perform injection so that when this call returns all dependencies will be available for use.
        ((DemoApplication) getApplication()).component().inject(this);
    }
}