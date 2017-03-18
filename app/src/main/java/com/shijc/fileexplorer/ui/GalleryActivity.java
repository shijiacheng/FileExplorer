package com.shijc.fileexplorer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.GalleryFolderAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.lv_folder)
    ListView lvFolder;
    private Context mContext;

    private GalleryPhotoAdapter mPhotoAdapter;
    private GalleryFolderAdapter mFolderAdapter;

    private List<PhotoFolderInfo> mAllPhotoFolderList = new ArrayList<>();
    private List<PhotoInfo> mCurPhotoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        mContext = this;

        mPhotoAdapter = new GalleryPhotoAdapter(mContext, ScreenUtils.getScreenWidth(mContext));
        gvPhotoList.setAdapter(mPhotoAdapter);

        mFolderAdapter = new GalleryFolderAdapter(mContext);
        lvFolder.setAdapter(mFolderAdapter);


        getPhotos();

        llFolderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llFolderList.getVisibility() == View.VISIBLE) {
                    llFolderList.setVisibility(View.GONE);
                    llFolderList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.gf_flip_horizontal_out));
                } else {
                    llFolderList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.gf_flip_horizontal_in));
                    llFolderList.setVisibility(View.VISIBLE);
                }
            }
        });

        mFolderAdapter.setListener(new GalleryFolderAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, PhotoFolderInfo info) {
                mCurPhotoList.clear();
                if (!CollectionUtils.isEmpty(info.getPhotoList())){
                    mCurPhotoList.addAll(info.getPhotoList());
                }
                mPhotoAdapter.setList(mCurPhotoList);

                widgetHeaderTitle.setText(info.getFolderName());

                llFolderList.setVisibility(View.GONE);
                llFolderList.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.gf_flip_horizontal_out));
            }
        });

        mPhotoAdapter.setListener(new GalleryPhotoAdapter.OnPreviewListener() {
            @Override
            public void onPhotoClick(View view, int position) {
                Intent intent = new Intent(mContext,GalleryPreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photolist", (Serializable) mCurPhotoList);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    mFolderAdapter.setList(mAllPhotoFolderList);

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
