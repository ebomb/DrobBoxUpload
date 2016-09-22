package com.demo.dropboxupload.async_tasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Eli on 9/21/2016.
 * Async Task that uploads file to specified server
 */

public class UploadFileTask extends AsyncTask<String, Void, FileMetadata> {

    private DbxClientV2 mDropboxClient = null;
    private final Callback mCallback;
    private Object mObject = null;
    private Exception mException;

    public interface Callback {
        void onUploadComplete(FileMetadata result);
        void onError(Exception e);
    }

    // Constructor for Dropbox
    public UploadFileTask(DbxClientV2 dbxClient, Callback callback) {
        mDropboxClient = dbxClient;
        mCallback = callback;
    }

    // Constructor for Box
    public UploadFileTask(Object object, Callback callback) {
        mObject = object;
        mCallback = callback;
    }

    @Override
    protected FileMetadata doInBackground(String... params) {
        // Get Image File
        String filePath = params[0];
        File localFile = new File(filePath);

        // Get File Path & Name
        String remoteFolderPath = params[1];
        String remoteFileName = localFile.getName();

        // Perform Upload Action
        try (InputStream inputStream = new FileInputStream(localFile)) {
            if (mDropboxClient != null) {
                // DROPBOX
                return mDropboxClient.files()
                        .uploadBuilder(remoteFolderPath + "/" + remoteFileName)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(inputStream);
            } else if (mObject != null) {
                // BOX
                // TODO: Box.com upload
            }
        } catch (DbxException | IOException e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(FileMetadata result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else if (result == null) {
            mCallback.onError(null);
        } else {
            mCallback.onUploadComplete(result);
        }
    }
}