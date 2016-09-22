package com.demo.dropboxupload.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Eli on 9/21/2016.
 * Utility class that helps with File functions
 */

public class FileUtils {

    /**
     * Gets the file path from Uri
     *
     * @param uri The uri of the saved file
     * @param context The context of the application
     */
    public static String getFilePath(Uri uri, Context context) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }
}
