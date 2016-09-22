package com.demo.dropboxupload.async_tasks;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.BoxException;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Eli on 9/21/2016.
 * Async Task that uploads file to dropbox or box
 */

public class UploadFileTask extends AsyncTask<String, Void, Void> {

    private DbxClientV2 mDropboxClient = null;
    private final Callback mCallback;
    private BoxApiFile mBoxApiFile;
    private Exception mException;

    // Handles callback from upload action
    public interface Callback {
        void onUploadComplete();
        void onError(Exception e);
    }

    // Constructor for Dropbox
    public UploadFileTask(DbxClientV2 dbxClient, Callback callback) {
        this.mDropboxClient = dbxClient;
        this.mBoxApiFile = null;
        this.mCallback = callback;
    }

    // Constructor for Box
    public UploadFileTask(BoxApiFile boxApiFile, Callback callback) {
        this.mDropboxClient = null;
        this.mBoxApiFile = boxApiFile;
        this.mCallback = callback;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(String... params) {
        // Get Image File
        String filePath = params[0];
        File localFile = new File(filePath);

        // Get File Path & Name
        String remoteFolderPath = params[1];
        String remoteFileName = localFile.getName();

        // Perform Upload Action
        try (InputStream inputStream = new FileInputStream(localFile)) {
            if (mDropboxClient != null) {
                mDropboxClient.files()
                        .uploadBuilder(remoteFolderPath + "/" + remoteFileName)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(inputStream);
            } else if (mBoxApiFile != null) {
                mBoxApiFile.getUploadRequest(inputStream, remoteFileName, remoteFolderPath).send();
            }
        } catch (DbxException | IOException | BoxException e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onUploadComplete();
        }
    }
}