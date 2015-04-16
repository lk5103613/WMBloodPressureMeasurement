package com.wm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.testin.agent.TestinAgent;
import com.wm.utils.PropertiesSharePrefs;

public class WelcomeActivity extends Activity implements
		OnCheckedChangeListener, OnClickListener {

	public final static String NAME = "loginName";
	public final static String  PASSWORD="loginPwd";

	@InjectView(R.id.welcome_logo)
	ImageView mLogo;
	@InjectView(R.id.welcome_company_name)
	ImageView mCompanyName;

	private Context mContext;
	private PropertiesSharePrefs mState;
	private AlertDialog mAalertDialog;
	private View mDialogView;
	private CheckBox mAuthCheckBox;
	private Button mBtnYes, mBtnNo;
	private TextView mLinkText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.init(this, "c4b338451aae8a2acce515ab0ccdc005", "wmbug");
		setContentView(R.layout.activity_welcome);
		ButterKnife.inject(this);
		
		mContext = this;
		mState = PropertiesSharePrefs.getInstance(mContext);

		Animation logoAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.welcome_logo_anim);
		logoAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Animation shakeAnimation = AnimationUtils.loadAnimation(
						mContext, R.anim.welcome_logo_shake);
				mLogo.startAnimation(shakeAnimation);
			}
		});
		mLogo.startAnimation(logoAnimation);
		Animation companyNameAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.welcome_company_name_anim);
		mCompanyName.startAnimation(companyNameAnimation);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				//判断如果同意之后， 不在提示
				boolean auth = mState.getProperty(PropertiesSharePrefs.TYPE_AUTH, false);
//				if (!auth) {
					showAuthDialog();
//				} else {
//					jumpPage();
//				}
				
			}
		}, 3000);
		
		
	}

	@SuppressLint("InflateParams")
	public void showAuthDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		LayoutInflater inflater = getLayoutInflater();
		mDialogView = inflater.inflate(R.layout.dialog_authority, null);

		mAuthCheckBox = (CheckBox) mDialogView.findViewById(R.id.auth_checkbox);
		mLinkText = (TextView) mDialogView.findViewById(R.id.link);
		mBtnNo = (Button) mDialogView.findViewById(R.id.btn_no);
		mBtnYes = (Button) mDialogView.findViewById(R.id.btn_yes);
		mAalertDialog = builder.create();
		mAalertDialog.setCanceledOnTouchOutside(false);
		mAalertDialog.setView(mDialogView, 0, 0, 0, 0);
		mAalertDialog.show();

		mAuthCheckBox.setOnCheckedChangeListener(this);
		mBtnNo.setOnClickListener(this);
		mBtnYes.setOnClickListener(this);

		String url = "<a href=\"http://www.leadingtechmed.cn/agreements/dataUpload.html\">"
				+ mLinkText.getText() + "</a> ";
		mLinkText.setText(Html.fromHtml(url));
		mLinkText.setMovementMethod(LinkMovementMethod.getInstance());

		Window dialogWindow = mAalertDialog.getWindow();
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

		WindowManager.LayoutParams params = dialogWindow.getAttributes();

		Point p = new Point();
		d.getSize(p);
		params.width = (int) (p.x * 0.9); // 宽度设置为屏幕的0.65

		dialogWindow.setAttributes(params);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mBtnYes.setEnabled(true);
		} else {
			mBtnYes.setEnabled(false);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			mState.saveProperty(PropertiesSharePrefs.TYPE_AUTH, true);
			break;
		case R.id.btn_no:
			mState.saveProperty(PropertiesSharePrefs.TYPE_AUTH, false);
			break;
		default:
			mState.saveProperty(PropertiesSharePrefs.TYPE_AUTH, false);
			break;
		}
		mAalertDialog.dismiss();
		jumpPage();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (isFinishing()) {
			overridePendingTransition(R.anim.scale_fade_in,
					R.anim.slide_out_to_left);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;// 禁止back
	}
	
	private void jumpPage(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		
	}

}
