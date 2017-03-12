package com.shijc.fileexplorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.model.ClassifyFileResult;

import java.util.List;

/**
 * Class:ClassifyFileAdapter
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class ClassifyFileAdapter extends BaseAdapter {

    private Context mContext;
    private List<ClassifyFileResult> list;

    public ClassifyFileAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setList(List<ClassifyFileResult> list) {
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
        final ClassifyFileResult result = list.get(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_classify_file, null);
            viewHolder.llClassifyBox = (LinearLayout) convertView.findViewById(R.id.ll_classify_box);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.iv_cover);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvSize = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.llClassifyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClick(result);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private ImageView ivCover;
        private TextView tvTitle;
        private TextView tvSize;
        private LinearLayout llClassifyBox;
    }

    public interface ClickListener{
        void onClick(ClassifyFileResult result);
    }

    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
