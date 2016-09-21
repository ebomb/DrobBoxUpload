package com.demo.dropboxupload.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Eli on 9/21/2016.
 */

public class DBUApplication extends Application {
//    @Inject Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        DBUInject.initialize(ObjectGraph.create(new DBUModule(this)));
//        DBUInject.inject(this);
    }
}
