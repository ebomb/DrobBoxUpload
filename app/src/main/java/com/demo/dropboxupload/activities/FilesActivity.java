package com.demo.dropboxupload.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.box.androidsdk.content.BoxApiFile;
import com.demo.dropboxupload.R;
import com.demo.dropboxupload.async_tasks.UploadFileTask;
import com.demo.dropboxupload.di.DemoApplication;
import com.demo.dropboxupload.singletons.DropboxClientFactory;
import com.demo.dropboxupload.utils.FileUtils;

import javax.inject.Inject;

import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_BOX;
import static com.demo.dropboxupload.utils.AppConstants.UPLOAD_TYPE_DROPBOX;

public class FilesActivity extends AppCompatActivity {

    private static final String EXTRA_PATH = "EXTRA_PATH", UPLOAD_TYPE = "UPLOAD_TYPE";
    private static final int PICKED_FILE_REQUEST_CODE = 1;
    ProgressDialog dialog;
    int uploadType;
    String mFolderName;

    @Inject Context mContext;
    @Inject BoxApiFile mBoxApiFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        ((DemoApplication) getApplication()).component().inject(this);  // Inject dependencies
        mFolderName = getIntent().getStringExtra(EXTRA_PATH);
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
        startActivityForResult(intent, PICKED_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKED_FILE_REQUEST_CODE) {
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

    @Override
    public void onRequestPermissionsResult(int actionCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // CHECK IF USER DENIED PERMISSION
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.read_access_denied, Toast.LENGTH_LONG).show();
                exitActivity();
                return;
            }
        }

        // USER GRANTED PERMISSION
        launchFilePicker();
    }

    // Performs the upload action to Box
    private void uploadBoxFile(String filePath) {
        displayProgressDialog(getString(R.string.uploading));

        // Start Upload Task
        new UploadFileTask(mBoxApiFile, new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete() {
                handleSuccessfulUpload();
            }

            @Override
            public void onError(Exception e) {
                handleErrorUpload();
            }
        }).execute(filePath, mFolderName);
    }

    // Performs the upload action to Dropbox
    private void uploadDropboxFile(String filePath) {
        displayProgressDialog(getString(R.string.uploading));

        // Start Upload Task
        new UploadFileTask(DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete() {
                handleSuccessfulUpload();
            }

            @Override
            public void onError(Exception e) {
                handleErrorUpload();
            }
        }).execute(filePath, mFolderName);
    }

    // Display success message and exit out of this Activity
    private void handleSuccessfulUpload() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Toast.makeText(mContext, R.string.successfully_uploaded, Toast.LENGTH_LONG).show();
        exitActivity();
    }

    // Display error message and exit out of this Activity
    private void handleErrorUpload() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Toast.makeText(mContext, R.string.error_has_occurred, Toast.LENGTH_SHORT).show();
        exitActivity();
    }

    // Display progress dialog with custom message
    private void displayProgressDialog(String message) {
        dialog = new ProgressDialog(FilesActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(message);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                exitActivity();
            }
        });
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
        if (!isFinishing()) {
            finish();
        }
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

    // Enum to generalize action type (in case new features come in)
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
