package com.demo.dropboxupload.app;

import android.content.Context;

import com.demo.dropboxupload.activities.UploadActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eli on 9/21/2016.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(UploadActivity baseActivity);

    Context context();
}