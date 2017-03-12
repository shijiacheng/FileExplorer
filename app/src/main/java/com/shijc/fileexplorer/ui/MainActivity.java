package com.shijc.fileexplorer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.adapter.ClassifyFileAdapter;
import com.shijc.fileexplorer.base.BaseActivity;
import com.shijc.fileexplorer.common.Constants;
import com.shijc.fileexplorer.model.ClassifyFileResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.ll_serach_box)
    LinearLayout llSerachBox;
    @BindView(R.id.gv_file)
    GridView gvClassifyFile;
    @BindView(R.id.ll_sdcard)
    LinearLayout llSdcard;


    private Context mContext;
    private ClassifyFileAdapter mAdapter;
    private List<ClassifyFileResult> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initViews();

        initDatas();

    }

    private void initViews(){
        mAdapter = new ClassifyFileAdapter(mContext);
        gvClassifyFile.setAdapter(mAdapter);

        mAdapter.setListener(new ClassifyFileAdapter.ClickListener() {
            @Override
            public void onClick(ClassifyFileResult result) {
                Intent intent = new Intent(mContext,ClassifyActivity.class);
                intent.putExtra(Constants.KEY_EXTRA_FILE_TYPE,result.getKey());
                startActivity(intent);
            }
        });

        llSdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DirectoryActivity.class);
                startActivity(intent);
            }
        });

        llSerachBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SearchFileActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initDatas(){
        lists = new ArrayList<>();
        lists.add(new ClassifyFileResult(ClassifyFileResult.PICTURE,"照片"));
        lists.add(new ClassifyFileResult(ClassifyFileResult.MUSIC,"music"));
        lists.add(new ClassifyFileResult(ClassifyFileResult.VIDEO,"video"));
        lists.add(new ClassifyFileResult(ClassifyFileResult.DOCUMENT,"docs"));
        lists.add(new ClassifyFileResult(ClassifyFileResult.ZIP,"zips"));
        lists.add(new ClassifyFileResult(ClassifyFileResult.APK,"apks"));

        mAdapter.setList(lists);
    }
}
