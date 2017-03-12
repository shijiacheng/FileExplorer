package com.shijc.fileexplorer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class:DirectoryAdapter
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class DirectoryAdapter extends BaseAdapter {
    private Context mcontext = null;
    private List<File> mfiles = null;
    private List<String> mSelectFiles = null;

    // 异步更新
    private Map<ImageView, Bitmap> imageViews;
    private ExecutorService pool;
    private boolean isSelect = true;

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView imageView = (ImageView) msg.obj;
            imageView.setBackgroundDrawable(new BitmapDrawable(mcontext.getResources(), imageViews.get(imageView)));
        }
    };

    public DirectoryAdapter(Context context, List<File> files, List<String> selectedFiles) {
        mcontext = context;
        mfiles = files;
        mSelectFiles = selectedFiles;
        pool = Executors.newFixedThreadPool(5); // 固定线程池
        imageViews = new HashMap<ImageView, Bitmap>();
    }

    @Override
    public int getCount() {
        int msize = 0;
        if (mfiles != null)
            msize = mfiles.size();
        return msize;
    }

    @Override
    public File getItem(int position) {
        if ((position >= 0) && (position < this.getCount()))
            return mfiles.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListHolder mListHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_directory, null);
            mListHolder = new ListHolder();
            mListHolder.mfileIcon = (ImageView) convertView.findViewById(R.id.listview_fileicon);
            mListHolder.mfileName = (TextView) convertView.findViewById(R.id.listview_filename);
            mListHolder.mfileSize = (TextView) convertView.findViewById(R.id.listview_filesize);
            mListHolder.mfileTime = (TextView) convertView.findViewById(R.id.listview_filetime);
            mListHolder.mfileSelect = (Button) convertView.findViewById(R.id.listview_select);
            mListHolder.divider_size_time = convertView.findViewById(R.id.divider_size_time);
            convertView.setTag(mListHolder);
        } else {
            mListHolder = (ListHolder) convertView.getTag();
        }
        File f = this.getItem(position);
        mListHolder.mfileName.setText(f.getName());
        if (!isSelect){
            mListHolder.mfileSelect.setVisibility(View.GONE);
            if (f.isFile()){
                mListHolder.divider_size_time.setVisibility(View.VISIBLE);
                mListHolder.mfileSize.setText(FileUtils.formatFileSize(f.length()));
                setFileIcon(f, mListHolder.mfileIcon);
            }else{
                mListHolder.divider_size_time.setVisibility(View.GONE);
                mListHolder.mfileSelect.setVisibility(View.GONE);
                mListHolder.mfileSize.setText("");
                mListHolder.mfileIcon.setBackgroundResource(R.drawable.icon_file_folder);
            }
        }else{
            if (f.isFile()) {
                mListHolder.divider_size_time.setVisibility(View.VISIBLE);
                mListHolder.mfileSelect.setVisibility(View.VISIBLE);
                if (mSelectFiles.contains(f.getAbsolutePath())) {
                    mListHolder.mfileSelect.setSelected(true);
//				mListHolder.mfileSelect.setBackgroundResource(R.drawable.icon_cb_checked);
                } else {
                    mListHolder.mfileSelect.setSelected(false);
//				mListHolder.mfileSelect.setBackgroundResource(R.drawable.icon_cb_unchecked);
                }
                mListHolder.mfileSize.setText(FileUtils.formatFileSize(f.length()));
                setFileIcon(f, mListHolder.mfileIcon);
            } else {
                mListHolder.divider_size_time.setVisibility(View.GONE);
                mListHolder.mfileSelect.setVisibility(View.GONE);
                mListHolder.mfileSize.setText("");
                mListHolder.mfileIcon.setBackgroundResource(R.drawable.icon_file_folder);

            }
        }

        mListHolder.mfileTime.setText(this.getFileTime(f.lastModified()));

        return convertView;
    }

    private void setFileIcon(final File f, final ImageView im) {
        if (f.exists()) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap;
                    String path = f.getAbsolutePath();
                    String extString = FileUtils.getExt(path);
                    if ("apk".equals(extString)){
                        bitmap = getApkIcon(path);
                    }else{
                        bitmap = FileUtils.getFileBm(mcontext,extString);
                    }
					/*if (Attachment.Type.APK == getFileType(extString)) {

					} else if (Attachment.Type.DOC == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_doc);
					} else if (Attachment.Type.LINK == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.file_icon_unknow);
					} else if (Attachment.Type.PDF == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_pdf);
					} else if (Attachment.Type.PNG == getFileType(extString)) {
						// 照片缩略图
						bitmap = getImageThumbnail(path, 96, 96);
					} else if (Attachment.Type.PPT == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_ppt);
					} else if (Attachment.Type.TXT == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_txt);
					} else if (Attachment.Type.VIDEO == getFileType(extString)) {
						// 視頻缩略图
						bitmap = getVideoThumbnail(path, 96, 96, MediaStore.Video.Thumbnails.MICRO_KIND);
					} else if (Attachment.Type.XLS == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_xls);
					} else if (Attachment.Type.ZIP == getFileType(extString)) {
						bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.icon_file_rar);
					}*/
                    if (null != bitmap) {
                        imageViews.put(im, bitmap);
                        Message message = handler.obtainMessage();
                        message.obj = im;
                        handler.sendMessage(message);
                    }
                }
            });

        }
    }

    public Bitmap getApkIcon(String path) {
        PackageManager pm = mcontext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        Bitmap bitmap = null;
        Drawable drawable = null;
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;

            if (Build.VERSION.SDK_INT >= 8) {
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
            }
            drawable = appInfo.loadIcon(pm);
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.file_icon_unknow);
        }
        return bitmap;
    }

    public String getFileTime(long filetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ftime = formatter.format(new Date(filetime));
        return ftime;
    }

    private static class ListHolder {
        ImageView mfileIcon;
        Button mfileSelect;
        TextView mfileName;
        TextView mfileSize;
        TextView mfileTime;
        View divider_size_time;
    }



}
