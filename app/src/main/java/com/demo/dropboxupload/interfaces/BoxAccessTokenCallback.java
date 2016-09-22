package com.demo.dropboxupload.interfaces;

import com.demo.dropboxupload.models.BoxAccess;

/**
 * Created by Eli on 9/22/2016.
 */

public interface BoxAccessTokenCallback {
    void onComplete(BoxAccess accessToken);

    void onError(String errorMessage);
}
