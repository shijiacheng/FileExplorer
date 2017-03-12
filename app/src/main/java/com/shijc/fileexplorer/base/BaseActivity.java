package com.shijc.fileexplorer.base;

import android.support.v4.app.FragmentActivity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;

import com.shijc.fileexplorer.R;
import com.shijc.fileexplorer.common.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * Class:BaseActivity
 * Description: Activity基类
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class BaseActivity extends FragmentActivity {

    private SystemBarTintManager mTintManager;

    protected boolean showTitleBar = true;
    protected int tintColor = -1;
    public boolean hasPadding = true;

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }


    @Override
    public void setContentView(int layoutResID) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View v = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(v);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && showTitleBar) {
            initStatusBar(view);
        }
        //初始化butterknife
        ButterKnife.bind(this);
    }

    private void initStatusBar(View view) {
        mTintManager = new SystemBarTintManager(this);

        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        if (tintColor == -1) {
            tintColor = getResources().getColor(R.color.main_text_color);
        }

        mTintManager.setTintColor(tintColor);

        if (hasPadding)
            view.setPadding(0, mTintManager.getConfig().getStatusBarHeight(), 0, 0);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
