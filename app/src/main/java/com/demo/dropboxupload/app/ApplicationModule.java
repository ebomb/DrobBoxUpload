package com.demo.dropboxupload.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        this.mApplication = application;
    }

    /**
     * Allow the application context to be injected
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    /**
     * Allow the Volley's RequestQueue to be injected
     */
    @Singleton
    @Provides
    RequestQueue provideRequestQueue(Application application) {
        return Volley.newRequestQueue(application);
    }

}
