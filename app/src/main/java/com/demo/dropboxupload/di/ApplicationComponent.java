package com.demo.dropboxupload.di;

import com.demo.dropboxupload.activities.BaseActivity;
import com.demo.dropboxupload.activities.UploadActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eli on 9/21/2016.
 */

@Singleton
@Component(modules = AndroidModule.class)
public interface ApplicationComponent {
    void inject(DemoApplication application);

    void inject(UploadActivity homeActivity);

    void inject(BaseActivity baseActivity);
}