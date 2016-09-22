package com.demo.dropboxupload.models;

/**
 * Created by Eli on 9/22/2016.
 * Model class that holds the values from requesting an
 * Access Token from Box after granting access to it
 */

public class BoxAccess {

    String accessToken;
    String refreshToken;
    String errorMessage;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static class Keys {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ERROR_DESCRIPTION = "error_description";
    }
}
