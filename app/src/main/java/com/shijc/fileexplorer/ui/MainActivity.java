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

    }


    private void initDatas(){
        lists = new ArrayList<>();
        lists.add(new ClassifyFileResult());
        lists.add(new ClassifyFileResult());
        lists.add(new ClassifyFileResult());
        lists.add(new ClassifyFileResult());
        lists.add(new ClassifyFileResult());
        lists.add(new ClassifyFileResult());

        mAdapter.setList(lists);
    }
}
