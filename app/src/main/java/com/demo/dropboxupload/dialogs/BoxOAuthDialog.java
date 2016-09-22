package com.demo.dropboxupload.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.models.BoxAppData;

import butterknife.ButterKnife;

/**
 * Created by Eli on 9/22/2016.
 * Dialog class that displays and handles the login to Box
 */

public class BoxOAuthDialog extends Dialog {

    private String mUrl = "";
    private final String question = "?", EQUALS = "=", AND = "&", CODE = "code";
    private final String SUCCESS_CODE = "success123", ACCESS_CODE_ID = "code=", ERROR_ID = "error=";
    private final BoxAuthCallback mBoxAuthCallback;

    public interface BoxAuthCallback {
        void onComplete(String authCode);

        void onError(String errorMessage);
    }

    public BoxOAuthDialog(Activity context, BoxAppData mBoxAppData, BoxAuthCallback boxAuthCallback) {
        super(context);
        this.mBoxAuthCallback = boxAuthCallback;
        this.mUrl = mBoxAppData.getAuthURI() + question
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
        ButterKnife.bind(this);

        WebView container = (WebView) findViewById(R.id.web_container);
        container.loadUrl(mUrl);
        container.getSettings().setJavaScriptEnabled(true);
        container.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("URL", url);
                if (url.contains(ACCESS_CODE_ID)) {
                    dismiss();
                    String authCode = url.substring(url.indexOf(ACCESS_CODE_ID)
                            + ACCESS_CODE_ID.length(), url.length()).trim();
                    mBoxAuthCallback.onComplete(authCode);
                } else if (url.contains(ERROR_ID)) {
                    dismiss();
                    String errorMessage = url.substring(url.indexOf(ERROR_ID)
                            + ERROR_ID.length(), url.indexOf("&"));
                    mBoxAuthCallback.onError(errorMessage);
                }
            }
        });
    }
}
