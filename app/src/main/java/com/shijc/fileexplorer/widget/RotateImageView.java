package com.shijc.fileexplorer.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.shijc.fileexplorer.R;


/**
 * 支持点击效果的图片控件
 *
 * @version 1.0.0
 * @create 2013年8月1日
 */
public class RotateImageView extends ImageView {
    ObjectAnimator anim;
    private boolean isEnableRotate;

    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    /**
     * 初始化控件的界面
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView);
//		isWidthFillScreen = a.getBoolean(
//				R.styleable.RotateImageView_is_width_fill_screen, false);
//		isHeightFillScreen = a.getBoolean(
//				R.styleable.RotateImageView_is_height_fill_screen, false);
        isEnableRotate = a.getBoolean(R.styleable.RotateImageView_enableRotate, true);
//		isSyncClick = a.getBoolean(R.styleable.RotateImageView_syncClicked, false);
//		if(isWidthFillScreen){
//			windowWidth = PhoneUtils.getWindowWidth((Activity)context);
//			vWidth = windowWidth;
//		}
//		if(isHeightFillScreen){
//			windowHeight = PhoneUtils.getWindowHeight((Activity)context);
//			vHeight = windowHeight;
//		}
        a.recycle();
    }

    @Override
    public boolean performClick() {
        if (!isEnableRotate) return super.performClick();
        if (anim != null && anim.isRunning()) {
            return false;
        } else {
            anim = ObjectAnimator.ofFloat(this, "rotate", 0.8f, 1.0f).setDuration(100);
            anim.start();
            anim.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    RotateImageView.this.setScaleX(value);
                    RotateImageView.this.setScaleY(value);
                    RotateImageView.this.setAlpha(value);
                }
            });
            RotateImageView.super.performClick();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //在按下事件中设置滤镜
                setFilter();
                break;
            case MotionEvent.ACTION_UP:
                //由于捕获了Touch事件，需要手动触发Click事件
                performClick();
            case MotionEvent.ACTION_CANCEL:
                //在CANCEL和UP事件中清除滤镜
                removeFilter();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置滤镜
     */
    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
    }

    /**
     * 清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }


}