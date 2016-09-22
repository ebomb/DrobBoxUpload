package com.demo.dropboxupload.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.demo.dropboxupload.R;
import com.demo.dropboxupload.async_tasks.UploadFileTask;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.FileUtils;
import com.dropbox.core.v2.files.FileMetadata;

import javax.inject.Inject;

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class FilesActivity extends AppCompatActivity {

    private static final String EXTRA_PATH = "EXTRA_PATH", UPLOAD_TYPE = "UPLOAD_TYPE";
    private static final int PICKFILE_REQUEST_CODE = 1;
    String mPath;
    int uploadType;

    @Inject Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        ((DemoApplication) getApplication()).component().inject(this);  // Inject dependencies
        mPath = getIntent().getStringExtra(EXTRA_PATH);
        uploadType = getIntent().getIntExtra(UPLOAD_TYPE, 0);
        performWithPermissions(FileAction.UPLOAD);
    }

    // Checks if we need to ask for permissions for read access
    private void performWithPermissions(final FileAction action) {
        if (hasPermissionsForAction(action)) {
            launchFilePicker();
        } else {
            requestPermissionsForAction(action);
        }
    }

    // Launch intent to pick file for upload
    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String filePath = FileUtils.getFilePath(data.getData(), mContext);
                switch (uploadType) {
                    case UPLOAD_TYPE_DROPBOX:
                        uploadDropboxFile(filePath);
                        break;
                    case UPLOAD_TYPE_BOX:
                        uploadBoxFile(filePath);
                        break;
                    default:
                        exitActivity();
                }
            } else {
                exitActivity();
            }
        }
    }

    private void uploadBoxFile(String filePath) {

    }

    @Override
    public void onRequestPermissionsResult(int actionCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // CHECK IF USER HAS GIVEN AUTH
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.read_access_denied, Toast.LENGTH_LONG).show();
                exitActivity();
                return;
            }
        }

        // AGREED PERMISSION
        launchFilePicker();
    }

    // Performs the upload action to Dropbox
    private void uploadDropboxFile(String filePath) {
        // Display Uploading Message
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading");
        dialog.show();

        // Start Upload Task
        new UploadFileTask(DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                dialog.dismiss();
//                String message = result.getName() + " size " + result.getSize() + " modified " +
//                        DateFormat.getDateTimeInstance().format(result.getClientModified());
//                Log.d("Success", message);
                Toast.makeText(mContext, R.string.uploaded, Toast.LENGTH_SHORT).show();
                exitActivity();
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
//                Log.d("ERROR!", "Failed to upload file " + e);
                Toast.makeText(mContext, R.string.error_has_occurred, Toast.LENGTH_SHORT).show();
                exitActivity();
            }
        }).execute(filePath, mPath);
    }

    // Creates the launching intent for this Activity
    public static Intent getIntent(Context context, String path, int uploadType) {
        Intent filesIntent = new Intent(context, FilesActivity.class);
        filesIntent.putExtra(FilesActivity.EXTRA_PATH, path);
        filesIntent.putExtra(FilesActivity.UPLOAD_TYPE, uploadType);
        return filesIntent;
    }

    // Safely exits out of this Activity
    private void exitActivity() {
        LandingActivity.UPLOAD_TYPE = -1;
        finish();
    }

    // Checks if user has permission for specified file action
    private boolean hasPermissionsForAction(FileAction action) {
        for (String permission : action.getPermissions()) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // Requests permissions for specified file actions
    private void requestPermissionsForAction(FileAction action) {
        ActivityCompat.requestPermissions(this, action.getPermissions(), action.getCode());
    }

    private enum FileAction {
        //DOWNLOAD(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        UPLOAD(Manifest.permission.READ_EXTERNAL_STORAGE);

        private final String[] permissions;

        FileAction(String... permissions) {
            this.permissions = permissions;
        }

        public int getCode() {
            return ordinal();
        }

        public String[] getPermissions() {
            return permissions;
        }
    }
}
