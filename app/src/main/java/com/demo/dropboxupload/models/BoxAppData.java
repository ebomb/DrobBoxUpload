package com.demo.dropboxupload.models;

/**
 * Created by Eli on 9/22/2016.
 * Model class that hold the Box Developer App Details
 */

public class BoxAppData {

    private String authURI;
    private String clientID;
    private String clientSecret;
    private String redirectURI;

    public String getRedirectURI() {
        return redirectURI;
    }

    public String getAuthURI() {
        return authURI;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setAuthURI(String authURI) {
        this.authURI = authURI;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public static class Keys {
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET = "client_secret";
        public static final String AUTH_URI = "auth_uri";
        public static final String REDIRECT_URI = "redirect_uri";
        public static final String CODE = "code";
        public static final String RESPONSE_TYPE = "response_type";
        public static final String STATE = "state";
    }
}
