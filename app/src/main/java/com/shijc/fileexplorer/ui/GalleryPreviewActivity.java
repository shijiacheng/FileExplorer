package com.shijc.fileexplorer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.GalleryPreviewAdapter;
import com.shijc.fileexplorer.base.BaseActivity;
import com.shijc.fileexplorer.model.PhotoInfo;
import com.shijc.fileexplorer.widget.FixedViewPager;
import com.shijc.fileexplorer.widget.RotateImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryPreviewActivity extends BaseActivity {

    @BindView(R.id.widget_header_back)
    RotateImageView widgetHeaderBack;
    @BindView(R.id.vp_photo_list)
    FixedViewPager vpPhotoList;

    private Context mContext;
    private GalleryPreviewAdapter mPreviewAdapter;

    private List<PhotoInfo> list;
    private int curIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);
        ButterKnife.bind(this);
        mContext = this;

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        list = (List<PhotoInfo>) bundle.getSerializable("photolist");
        curIndex = bundle.getInt("curIndex");

        mPreviewAdapter = new GalleryPreviewAdapter(mContext,list);
        vpPhotoList.setAdapter(mPreviewAdapter);

        if (curIndex>0){
            vpPhotoList.setCurrentItem(curIndex,false);
        }

        vpPhotoList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
