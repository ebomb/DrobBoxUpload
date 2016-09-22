package com.demo.dropboxupload.models;

/**
 * Created by Eli on 9/20/2016.
 */

public class DropboxAppData {
    private String appKey;
    private String secretKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static class Keys {
        public static final String APP_KEY = "key";
        public static final String APP_SECRET = "secret";
    }
}
