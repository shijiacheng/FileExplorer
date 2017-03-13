package com.shijc.fileexplorer.ui;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.DirectoryAdapter;
import com.shijc.fileexplorer.base.BaseActivity;
import com.shijc.fileexplorer.util.CollectionUtils;
import com.shijc.fileexplorer.widget.RotateImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;


public class DirectoryActivity extends BaseActivity {

    @BindView(R.id.widget_header_back)
    RotateImageView headerBack;
    @BindView(R.id.widget_header_title)
    TextView headerTitle;
    @BindView(R.id.files_list)
    ListView mListView;
    @BindView(R.id.files_nofile)
    TextView mNoTextView;
    private List<File> mfiles = null;
    private ArrayList<String> mSelectedFiles = null;
    private DirectoryAdapter madapter = null;
    // String
    private String mSDCardPath = null;
    // String
    private File mCurrentPathFile = null;

    @BindView(R.id.ll_file_nav)
    LinearLayout ll_file_nav;//面包屑导航栏

    private String defaultRootFolderName;

    /**
     * 列表页地址面包屑导航栏
     */
    private void addNavView(String folderName, String folderId) {
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(10, 0, 0, 0);
        layoutparams.gravity = Gravity.CENTER_VERTICAL;
        TextView tv = new TextView(DirectoryActivity.this);
        tv.setLayoutParams(layoutparams);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(getResources().getColor(R.color.main_text_gray));
        tv.setTag(folderId);
        tv.setText(folderName + " >");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpId = (String) v.getTag();
                int index = ll_file_nav.indexOfChild(v);
                ll_file_nav.removeViews(index + 1, ll_file_nav.getChildCount() - index - 1);

                File f = new File(tmpId);
                showFilesInFolder(f);
            }
        });
        ll_file_nav.addView(tv);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_directory);
        if (mSDCardPath == null)
            checkEnvironment();
        defaultRootFolderName = getString(R.string.sdcard_rootpath_name);
        initView();
        initData();
    }

    public void initView() {

        headerBack.setVisibility(View.VISIBLE);
        headerTitle.setText("Directory");
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


    private void initData() {
        File file = null;
        // if (mCurrentPathFile != null) {
        // if (mCurrentPathFile.isDirectory()) {
        // file = mCurrentPathFile;
        // } else {
        // file = mCurrentPathFile.getParentFile();
        // }
        // } else {
        if (mSDCardPath != null) {
            file = new File(mSDCardPath);
        }
        // }

        if (file != null) {
            open(file);
        }
    }

    private void showFilesInFolder(File folder) {
        deleteAllItems();
        mCurrentPathFile = folder;

        File[] files = folder.listFiles();
        if (!CollectionUtils.isEmpty(files)) {
            // 排序
            List<File> list = Arrays.asList(files);
            Collections.sort(list, new FileComparator());
            for (File file : list) {
                if (file.isHidden() || !file.canRead()) {
                    continue;
                }
                addItem(file);
            }
            madapter.notifyDataSetChanged();
        }
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
//                if (mSelectedFiles.contains(f.getAbsolutePath())) {
//                    mSelectedFiles.remove(f.getAbsolutePath());
//                    mcompleteButton.setText(String.format(getString(R.string.files_checked_maxcount), mCount + mSelectedFiles.size(),maxCount));
//                } else {
//                    if (mSelectedFiles.size() + mCount < maxCount) {
//                        mSelectedFiles.add(f.getAbsolutePath());
//                        mcompleteButton.setText(String.format(getString(R.string.files_checked_maxcount), mCount+ mSelectedFiles.size(),maxCount));
//                    } else {
//                        FoxToast.showWarning(this,String.format(getString(R.string.choose_file_format),maxCount), Toast.LENGTH_SHORT);
//                    }
//
//                }
                madapter.notifyDataSetChanged();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        } else if (f.isDirectory()) {
            if (mSDCardPath.equals(f.getAbsolutePath()))
                addNavView(defaultRootFolderName, f.getAbsolutePath());
            else
                addNavView(f.getName(), f.getAbsolutePath());
            showFilesInFolder(f);
        }
    }

    /**
     * addItem:增加文件
     */
    private void addItem(File f) {
        if (f.isDirectory()) {
            mfiles.add(f);
        } else {
            if (f.isFile()
                    && f.getAbsolutePath().lastIndexOf(".") != -1
                    ) {
                mfiles.add(f);
            }

        }
    }

    //&& suffitString.contains(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".") + 1).toLowerCase()

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
//			File f = null;
//			f = Environment.getExternalStorageDirectory();// 获取sd卡目录
//			while (f != null && f.getParentFile() != null) {
//				f = f.getParentFile();
//				if (f.getAbsolutePath().endsWith("storage")) {
//					mSDCardPath = f.getAbsolutePath();
//					break;
//				}
//			}
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (viewModel == MODEL_PREVIEW)
            return super.onKeyDown(keyCode,event);*/
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentPathFile == null) {
                return true;
            }
            if (mSDCardPath.equals(mCurrentPathFile.getAbsolutePath())) {
//                setResultData();//根目录再点击返回，离开这个页面
                finish();
            } else {
                if (null != ll_file_nav && ll_file_nav.getChildCount() > 0) {
                    showFilesInFolder(mCurrentPathFile.getParentFile());
                    ll_file_nav.removeViews(ll_file_nav.getChildCount() - 1, 1);//移除导航栏最后一项
                }
            }
        }
        return true;
    }

    /**
     * 排序 *
     */
    private class FileComparator implements Comparator<File> {

        public int compare(File file1, File file2) {


            if (file1.isDirectory() == true && file2.isDirectory() == true) { // 再比较文件夹
                return file1.getName().compareToIgnoreCase(file2.getName());
            } else {
                if ((file1.isDirectory() && !file2.isDirectory()) == true) {
                    return -1;
                } else if ((file2.isDirectory() && !file1.isDirectory()) == true) {
                    return 1;
                } else {
                    return file1.getName().compareToIgnoreCase(file2.getName());// 最后比较文件
                }
            }


        }
    }
}
