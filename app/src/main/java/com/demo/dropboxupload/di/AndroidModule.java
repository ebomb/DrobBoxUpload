package com.demo.dropboxupload.di;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eli on 9/21/2016.
 */

@Module
public class AndroidModule {
    private final DemoApplication application;

    public AndroidModule(DemoApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected.
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    /**
     * Allow Volley's RequestQueue to be injected.
     */
    @Provides
    @Singleton
    RequestQueue provideRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }
}