package com.shijc.fileexplorer.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shijc.fileexplorer.R;


public class BottomPopuWindow extends PopupWindow {
	protected Activity context;
	View parent;
	int textColor;

    private String title;
    private String content;

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public BottomPopuWindow(Context context){
		super(context);
	}

	public BottomPopuWindow(Activity context, View parent) {
		this.context = context;
		this.parent = parent;
		setContentView(getView());
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new ColorDrawable());
		setAnimationStyle(R.style.SharePopWindowStyle);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				setWindowAlpha(1.0f);
			}
		});
	}

	private void setWindowAlpha(float alpha){
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = alpha;
		context.getWindow().setAttributes(params);
	}

	public View getView() {
		View view = View.inflate(context, R.layout.bottom_popuwindow, null);
         tv_title = (TextView) view.findViewById(R.id.title);
         tv_content = (TextView) view.findViewById(R.id.content);
         btn_ok = (Button) view.findViewById(R.id.btn_ok);
         btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        tv_title.setText(title);
        tv_content.setText(content);

        btn_ok.setOnClickListener(okClickListener);
        btn_cancel.setOnClickListener(cancelClickListener);

		return view;
	}

	public void show() {
		setWindowAlpha(0.6f);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}

	TextView tv_title;
	TextView tv_content;
	Button btn_ok;
	Button btn_cancel;

	private View.OnClickListener okClickListener;
    private View.OnClickListener cancelClickListener;

    public void setOkClickListener(View.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
		btn_ok.setOnClickListener(okClickListener);
    }

    public void setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
		btn_cancel.setOnClickListener(cancelClickListener);
    }

    public void setTitle(String title) {
        this.title = title;
		tv_title.setText(title);
    }

    public void setContent(String content) {
        this.content = content;
		tv_content.setText(content);
    }
}
