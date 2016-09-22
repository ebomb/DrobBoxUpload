package com.demo.dropboxupload.di;

import android.content.Context;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.models.BoxSession;
import com.demo.dropboxupload.models.BoxAppData;
import com.demo.dropboxupload.models.translators.BoxAppDataTranslator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Eli on 9/21/2016.
 * Module to manage my inject dependencies
 */

@Module
class AndroidModule {
    private final DemoApplication application;

    AndroidModule(DemoApplication application) {
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
     * Allow Box's Session to be injected.
     */
    @Provides
    @Singleton
    BoxSession provideBoxSession(Context context) {
        final BoxAppData mBoxAppData = BoxAppDataTranslator.getBoxAppData(context);
        return new BoxSession(context,
                "",
                mBoxAppData.getClientID(),
                mBoxAppData.getClientSecret(),
                mBoxAppData.getRedirectURI());
    }

    /**
     * Allow Box's FileApi to be injected.
     */
    @Provides
    @Singleton
    BoxApiFile provideBoxApiFile(BoxSession boxSession) {
        return new BoxApiFile(boxSession);
    }

}