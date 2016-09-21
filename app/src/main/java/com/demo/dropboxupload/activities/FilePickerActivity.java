package com.demo.dropboxupload.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.demo.dropboxupload.R;

public class FilePickerActivity extends AppCompatActivity {

    private static final String EXTRA_PATH = "file_picker_path";
    private static final int PICKFILE_REQUEST_CODE = 1;
    String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        mPath = getIntent().getStringExtra(EXTRA_PATH);
        launchFilePicker();
    }

    private void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    public static Intent getIntent(Context context, String path) {
        Intent filesIntent = new Intent(context, FilePickerActivity.class);
        filesIntent.putExtra(FilePickerActivity.EXTRA_PATH, path);
        return filesIntent;
    }
}
