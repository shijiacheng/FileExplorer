package com.shijc.fileexplorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.model.PhotoInfo;
import com.shijc.fileexplorer.util.PicassoUtils;

import java.util.List;

/**
 * Class:GalleryPhotoAdapter
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/15.
 */

public class GalleryPhotoAdapter extends BaseAdapter{

    private Context mContext;
    private List<PhotoInfo> list;
    private int mRowWidth;

    public GalleryPhotoAdapter(Context mContext,int screenWidth){
        this.mContext = mContext;
        this.mRowWidth = screenWidth/3;
    }

    public void setList(List<PhotoInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final PhotoInfo result = list.get(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_gallery_photo, null);
            viewHolder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PicassoUtils.loadImageViewSize(mContext,result.getPhotoPath(),mRowWidth,mRowWidth,viewHolder.ivThumb);


//        PicassoUtils.loadImageViewLocal(mContext,result.getPhotoPath(),R.drawable.cover_music,viewHolder.ivThumb);
        return convertView;
    }

    static class ViewHolder {
        private ImageView ivThumb;

    }
}
