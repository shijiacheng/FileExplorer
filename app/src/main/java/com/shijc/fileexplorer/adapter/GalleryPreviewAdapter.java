package com.shijc.fileexplorer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.model.PhotoInfo;
import com.shijc.fileexplorer.util.PicassoUtils;
import com.shijc.fileexplorer.widget.photoview.PhotoView;

import java.util.List;

/**
 * Class:GalleryPreviewAdapter
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/18.
 */

public class GalleryPreviewAdapter extends PagerAdapter{
    private Context mContext;
    private List<PhotoInfo> list;

    public GalleryPreviewAdapter(Context mContext,List<PhotoInfo> list){
        this.mContext = mContext;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_gallery_preview , null);
        PhotoView ivPreview = (PhotoView) v.findViewById(R.id.iv_preview);
        PicassoUtils.loadImageViewLocal(mContext,list.get(position).getPhotoPath(),R.drawable.icon_folder,ivPreview);


        container.addView(v);
        return v;
    }
}
