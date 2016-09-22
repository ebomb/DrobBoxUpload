package com.demo.dropboxupload.singletons;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.models.BoxSession;

/**
 * Created by Eli on 9/22/2016.
 */

public class BoxApiFileClientFactory {
    private static BoxApiFile boxApiFile;

    public static void init(BoxSession session) {
        if (boxApiFile == null) {
            boxApiFile = new BoxApiFile(session);
        }
    }

    public static BoxApiFile getClient() {
        return boxApiFile;
    }
}
