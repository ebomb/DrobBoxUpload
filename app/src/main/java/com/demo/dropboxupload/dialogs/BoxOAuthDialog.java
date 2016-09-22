package com.demo.dropboxupload.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.models.BoxAppData;

/**
 * Created by Eli on 9/22/2016.
 * Dialog class that displays and handles the login to Box
 * and requests to grant access to Box
 */

public class BoxOAuthDialog extends Dialog {

    private String authURL = "";
    private final String question = "?", EQUALS = "=", AND = "&";
    private final String SUCCESS_CODE = "success123", SUCCESS_CODE_ID = "code=", ERROR_ID = "error=";
    private final BoxAuthCallback mBoxAuthCallback;

    public interface BoxAuthCallback {
        void onComplete(String authCode);

        void onError(String errorMessage);
    }

    public BoxOAuthDialog(Activity context, BoxAppData mBoxAppData, BoxAuthCallback boxAuthCallback) {
        super(context);
        this.mBoxAuthCallback = boxAuthCallback;

        // Generate login URL for a GET request
        this.authURL = mBoxAppData.getAuthURI() + question
                + BoxAppData.Keys.RESPONSE_TYPE + EQUALS + BoxAppData.Keys.CODE + AND
                + BoxAppData.Keys.CLIENT_ID + EQUALS + mBoxAppData.getClientID() + AND
                + BoxAppData.Keys.REDIRECT_URI + EQUALS + mBoxAppData.getRedirectURI() + AND
                + BoxAppData.Keys.STATE + EQUALS + SUCCESS_CODE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_simple_webview);

        // Display Web View from generated URL in constructor
        WebView container = (WebView) findViewById(R.id.web_container);
        container.loadUrl(authURL);

        // Add a listener to detect whenever a new page finishes loading
        container.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Check if user granted access to Box
                if (url.contains(SUCCESS_CODE_ID)) {
                    dismiss();
                    String authCode = url.substring(url.indexOf(SUCCESS_CODE_ID) + SUCCESS_CODE_ID.length(), url.length()).trim();
                    mBoxAuthCallback.onComplete(authCode);
                } else if (url.contains(ERROR_ID)) {
                    dismiss();
                    String errorMessage = url.substring(url.indexOf(ERROR_ID) + ERROR_ID.length(), url.indexOf(AND));
                    mBoxAuthCallback.onError(errorMessage);
                }
            }
        });
    }
}