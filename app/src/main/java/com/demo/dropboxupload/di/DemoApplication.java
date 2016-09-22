package com.demo.dropboxupload.di;

import android.app.Application;

/**
 * Custom application definition which initiates the component
 */
public class DemoApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return component;
    }
}
