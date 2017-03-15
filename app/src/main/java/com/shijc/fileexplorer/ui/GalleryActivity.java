package com.shijc.fileexplorer.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.GalleryPhotoAdapter;
import com.shijc.fileexplorer.base.BaseActivity;
import com.shijc.fileexplorer.common.BaseTask;
import com.shijc.fileexplorer.common.HttpConnectException;
import com.shijc.fileexplorer.model.PhotoFolderInfo;
import com.shijc.fileexplorer.model.PhotoInfo;
import com.shijc.fileexplorer.util.CollectionUtils;
import com.shijc.fileexplorer.util.PhotoUtils;
import com.shijc.fileexplorer.util.ScreenUtils;
import com.shijc.fileexplorer.widget.RotateImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GalleryActivity extends BaseActivity {
    @BindView(R.id.gv_photo_list)
    GridView gvPhotoList;
    @BindView(R.id.widget_header_back)
    RotateImageView widgetHeaderBack;
    @BindView(R.id.widget_header_title)
    TextView widgetHeaderTitle;
    @BindView(R.id.ll_folder_name)
    RelativeLayout llFolderName;
    @BindView(R.id.ll_folder_list)
    RelativeLayout llFolderList;
    private Context mContext;

    private GalleryPhotoAdapter mPhotoAdapter;

    private List<PhotoFolderInfo> mAllPhotoFolderList = new ArrayList<>();
    private List<PhotoInfo> mCurPhotoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mContext = this;

        mPhotoAdapter = new GalleryPhotoAdapter(mContext, ScreenUtils.getScreenWidth(mContext));
        gvPhotoList.setAdapter(mPhotoAdapter);

        getPhotos();

        llFolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llFolderList.getVisibility() == View.VISIBLE ) {
                    llFolderList.setVisibility(View.GONE);
                    llFolderList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.gf_flip_horizontal_out));
                } else {
                    llFolderList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.gf_flip_horizontal_in));
                    llFolderList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取分类的数据
     */
    private BaseTask<Void, List<PhotoFolderInfo>> getFilesTask;

    private void getPhotos() {

        if (getFilesTask != null && getFilesTask.getStatus() == BaseTask.Status.RUNNING) {
            return;
        }

        getFilesTask = new BaseTask<Void, List<PhotoFolderInfo>>() {
            @Override
            protected List<PhotoFolderInfo> doInBackground(Void... params) {
                List<PhotoFolderInfo> result = null;
                try {
                    result = PhotoUtils.getAllPhotoFolder(GalleryActivity.this);
                } catch (Exception e) {
                    publishProgress(new HttpConnectException(e.getMessage()));
                    e.printStackTrace();
                }
                return result;
            }
        };

        getFilesTask.setTaskListener(new BaseTask.TaskListener<List<PhotoFolderInfo>>() {
            @Override
            public void onSucess(List<PhotoFolderInfo> result) {
                if (!CollectionUtils.isEmpty(result)) {
                    mAllPhotoFolderList.clear();
                    List<PhotoFolderInfo> allFolderList = result;
                    mAllPhotoFolderList.addAll(allFolderList);

                    mCurPhotoList.clear();
                    if (allFolderList.size() > 0) {
                        if (allFolderList.get(0).getPhotoList() != null) {
                            mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                        }
                    }

                    mPhotoAdapter.setList(mCurPhotoList);

                }
            }

            @Override
            public void onFailed(HttpConnectException exception) {
//                FoxToast.showWarning(AppContext.getInstance(), "获取已读数据失败！", Toast.LENGTH_SHORT);
            }
        });
        getFilesTask.asynExecute();
    }
}
