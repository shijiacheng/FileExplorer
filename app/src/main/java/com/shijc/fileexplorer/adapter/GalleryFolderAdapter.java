package com.shijc.fileexplorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.model.PhotoFolderInfo;
import com.shijc.fileexplorer.util.PicassoUtils;

import java.util.List;

/**
 * Class:GalleryFolderAdapter
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/18.
 */

public class GalleryFolderAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoFolderInfo> list;

    public GalleryFolderAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setList(List<PhotoFolderInfo> list) {
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
        final PhotoFolderInfo info = list.get(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_gallery_folder, null);
            viewHolder.mIvCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            viewHolder.mTvFolderName = (TextView) convertView.findViewById(R.id.tv_folder_name);
            viewHolder.mTvPhotoCount = (TextView) convertView.findViewById(R.id.tv_photo_count);
            viewHolder.mIvFolderCheck = (ImageView) convertView.findViewById(R.id.iv_folder_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PicassoUtils.loadImageViewLocal(mContext,info.getCoverPhoto().getPhotoPath(),R.drawable.icon_folder,viewHolder.mIvCover);

        viewHolder.mTvFolderName.setText(info.getFolderName());
        viewHolder.mTvPhotoCount.setText(info.getPhotoList().size()+"张");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(v,info);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView mIvCover;
        ImageView mIvFolderCheck;
        TextView mTvFolderName;
        TextView mTvPhotoCount;

    }

    public interface ClickListener{
        void onItemClick(View view,PhotoFolderInfo info);
    }
    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
