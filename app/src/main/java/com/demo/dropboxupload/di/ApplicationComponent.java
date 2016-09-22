package com.demo.dropboxupload.di;

import com.demo.dropboxupload.activities.BaseActivity;
import com.demo.dropboxupload.activities.FilesActivity;
import com.demo.dropboxupload.activities.LandingActivity;
import com.demo.dropboxupload.utils.BoxAuth;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Eli on 9/21/2016.
 * Application component that can know which classes are going to
 * need a dependency injection.
 */

@Singleton
@Component(modules = AndroidModule.class)
public interface ApplicationComponent {
    void inject(LandingActivity homeActivity);

//    void inject(BaseActivity baseActivity);

    void inject(FilesActivity filesActivity);

    void inject(BoxAuth boxAuth);
}