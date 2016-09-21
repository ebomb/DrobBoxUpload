package com.demo.dropboxupload.di;

import android.app.Application;


import com.android.volley.RequestQueue;

import javax.inject.Inject;

/**
 * Custom application definition.
 */
public class DemoApplication extends Application {

    @Inject RequestQueue mRequestQueue;

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
        component().inject(this);
    }

    public ApplicationComponent component() {
        return component;
    }
}
