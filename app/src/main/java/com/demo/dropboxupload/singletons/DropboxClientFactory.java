package com.demo.dropboxupload.singletons;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by Eli on 9/21/2016.
 * Singleton instance of {@link DbxClientV2} and friends
 */

public class DropboxClientFactory {
    private static DbxClientV2 sDbxClient;

    public static void init(String accessToken) {
        if (sDbxClient == null) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("DropboxUploader").build();
            sDbxClient = new DbxClientV2(requestConfig, accessToken);
        }
    }

    public static DbxClientV2 getClient() {
        return sDbxClient;
    }
}