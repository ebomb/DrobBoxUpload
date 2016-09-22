package com.demo.dropboxupload.singletons;

import android.content.Context;

import com.box.androidsdk.content.models.BoxSession;
import com.demo.dropboxupload.models.BoxAppData;

/**
 * Created by Eli on 9/22/2016.
 */
public class BoxSessionClientFactory {
    private static BoxSession boxSession;

    public static void init(BoxAppData boxAppData, Context context) {
        if (boxSession == null) {
            boxSession = new BoxSession(context,
                    "user_id",
                    boxAppData.getClientID(),
                    boxAppData.getClientSecret(),
                    boxAppData.getRedirectURI());
        }
    }

    public static BoxSession getClient() {
        return boxSession;
    }
}
