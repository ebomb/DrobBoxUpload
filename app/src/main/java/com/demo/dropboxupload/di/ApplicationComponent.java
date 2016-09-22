package com.demo.dropboxupload.di;

import com.demo.dropboxupload.activities.BaseActivity;
import com.demo.dropboxupload.activities.FilesActivity;
import com.demo.dropboxupload.activities.LandingActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eli on 9/21/2016.
 */

@Singleton
@Component(modules = AndroidModule.class)
public interface ApplicationComponent {
    void inject(DemoApplication application);

    void inject(LandingActivity homeActivity);

    void inject(BaseActivity baseActivity);

    void inject(FilesActivity filesActivity);
}