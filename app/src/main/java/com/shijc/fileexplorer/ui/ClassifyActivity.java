package com.shijc.fileexplorer.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.DirectoryAdapter;
import com.shijc.fileexplorer.base.BaseActivity;
import com.shijc.fileexplorer.common.BaseTask;
import com.shijc.fileexplorer.common.Constants;
import com.shijc.fileexplorer.common.HttpConnectException;
import com.shijc.fileexplorer.model.ClassifyFileResult;
import com.shijc.fileexplorer.util.CollectionUtils;
import com.shijc.fileexplorer.util.FileUtils;
import com.shijc.fileexplorer.widget.RotateImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ClassifyActivity extends BaseActivity {

    @BindView(R.id.widget_header_back)
    RotateImageView headerBack;
    @BindView(R.id.widget_header_title)
    TextView headerTitle;
    @BindView(R.id.files_list)
    ListView mListView;
    @BindView(R.id.files_nofile)
    TextView mNoTextView;
    private ContentResolver mContentResolver;
    private Context mContext;

    /* 传递的数据类型*/
    private int fileType;

    private List<File> mfiles = null;
    private ArrayList<String> mSelectedFiles = null;
    private DirectoryAdapter madapter = null;
    // String
    private String mSDCardPath = null;


    private void initView() {

        headerBack.setVisibility(View.VISIBLE);
        String title ="";
        switch (fileType) {
            case ClassifyFileResult.PICTURE:
                title = "照片";
                break;
            case ClassifyFileResult.MUSIC:
                title = "music";
                break;
            case ClassifyFileResult.VIDEO:
                title = "video";
                break;
            case ClassifyFileResult.DOCUMENT:
                title = "docs";
                break;
            case ClassifyFileResult.ZIP:
                title = "zips";
                break;
            case ClassifyFileResult.APK:
                title = "apks";
                break;
        }
        headerTitle.setText(title);
        headerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mfiles = new ArrayList<File>();
        mSelectedFiles = new ArrayList<String>();
        madapter = new DirectoryAdapter(this, mfiles, mSelectedFiles);
        madapter.setSelect(false);
        mListView.setAdapter(madapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File mselectedFile = madapter.getItem(position);
                if (mselectedFile != null) {
//                    if (viewModel == MODEL_PREVIEW){
//                        if (mselectedFile.isFile())
//                            FileUtils.openLocalFile(mselectedFile.getPath(), FileExp.this);
//                        else
//                            open(mselectedFile);
//                    }else{
//                        open(mselectedFile);
//                    }
                    open(mselectedFile);
                }
            }
        });
        mListView.setEmptyView(mNoTextView);
    }


    private void open(File f) {
        if (f == null)
            return;

        if (!f.exists())
            return;

        if (!f.canRead())
            return;

        if (f.isFile()) {
            try {

                FileUtils.openLocalFile(f.getAbsolutePath(),mContext);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private void deleteAllItems() {
        mfiles.clear();
        madapter.notifyDataSetChanged();
    }

    /**
     * checkEnvironment: Check the SDCard and the Root Path
     */
    private void checkEnvironment() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            mSDCardPath = Environment.getExternalStorageDirectory().toString();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        mContext = this;


        Intent intent = getIntent();
        fileType = intent.getIntExtra(Constants.KEY_EXTRA_FILE_TYPE, 0);

        mContentResolver = mContext.getContentResolver();


        if (mSDCardPath == null)
            checkEnvironment();
        initView();

        deleteAllItems();
        getFilesByType(fileType);
    }

    /**
     * 获取分类的数据
     */
    private BaseTask<Void, List<File>> getFilesTask;

    private void getFilesByType(final int type) {

        if (getFilesTask != null && getFilesTask.getStatus() == BaseTask.Status.RUNNING) {
            return;
        }

        getFilesTask = new BaseTask<Void, List<File>>() {
            @Override
            protected List<File> doInBackground(Void... params) {
                List<File> result = null;
                try {
                    switch (type) {
                        case ClassifyFileResult.PICTURE:
                            break;
                        case ClassifyFileResult.MUSIC:
                            result = getAllMusic();
                            break;
                        case ClassifyFileResult.VIDEO:
                            result = getAllVideo();
                            break;
                        case ClassifyFileResult.DOCUMENT:
                            result = getDocuments();
                            break;
                        case ClassifyFileResult.ZIP:
                            result = getAllZip();
                            break;
                        case ClassifyFileResult.APK:
                            break;
                    }
                } catch (Exception e) {
                    publishProgress(new HttpConnectException(e.getMessage()));
                    e.printStackTrace();
                }
                return result;
            }
        };

        getFilesTask.setTaskListener(new BaseTask.TaskListener<List<File>>() {
            @Override
            public void onSucess(List<File> result) {
                if (!CollectionUtils.isEmpty(result)) {
                    mfiles.addAll(result);
                    madapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(HttpConnectException exception) {
//                FoxToast.showWarning(AppContext.getInstance(), "获取已读数据失败！", Toast.LENGTH_SHORT);
            }
        });
        getFilesTask.asynExecute();
    }


    private List<File> getDocuments() {

        List<File> texts = new ArrayList<>();

        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE};

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";

        String[] selectionArgs = new String[]{"text/plain", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.ms-excel"};

        Cursor cursor = mContentResolver.query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");

        while (cursor.moveToNext()) {


            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

            File fileItem = new File(filePath);

            texts.add(fileItem);

        }


        cursor.close();
        cursor = null;


        return texts;

    }


    private List<File> getAllMusic() {

        List<File> musics = new ArrayList<>();


        String[] projection = new String[]{MediaStore.Audio.AudioColumns._ID, MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.DISPLAY_NAME};


        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Audio.AudioColumns.DATE_MODIFIED + " desc");


        while (cursor.moveToNext()) {


            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));


            File fileItem = new File(filePath);

            musics.add(fileItem);

        }

        cursor.close();

        cursor = null;

        return musics;

    }


    private List<File> getAllVideo() {

        List<File> videos = new ArrayList<>();


        String[] projection = new String[]{MediaStore.Video.VideoColumns._ID, MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DISPLAY_NAME};


        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Video.VideoColumns.DATE_MODIFIED + " desc");


        while (cursor.moveToNext()) {

            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));


            File fileItem = new File(filePath);

            videos.add(fileItem);

        }


        cursor.close();
        cursor = null;

        return videos;

    }


    private List<File> getAllZip() {

        List<File> zips = new ArrayList<>();


        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE};

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? ";

        String[] selectionArgs = new String[]{"application/zip"};

        Cursor cursor = mContentResolver.query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");


        while (cursor.moveToNext()) {

            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

            File fileItem = new File(filePath);

            zips.add(fileItem);

        }

        cursor.close();
        cursor = null;

        return zips;

    }
}
