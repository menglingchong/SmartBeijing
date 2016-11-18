package com.heima.smartbeijing;

import com.heima.smartbeijing.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * 闪屏页面
 * @author lenovo
 *
 */
public class SplashActivity extends Activity {

    private RelativeLayout rlRoot;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);      
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//动画时长
        rotateAnimation.setFillAfter(true);//保持动画的结束状态
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        
        //动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //启动动画
        rlRoot.startAnimation(animationSet);
        //动画的监听
        animationSet.setAnimationListener(new AnimationListener() {
			
			private Intent intent;

			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//动画结束监听
				//如果是第一次进入，跳转到新手引导页
				//如果不是第一次进入，跳转到主界面
				boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
				if (isFirstEnter) {
					//新手引导页
					intent = new Intent(getApplicationContext(), GuideActivity.class);
				} else {
					//跳转到主界面
					intent = new Intent(getApplicationContext(),MainActivity.class);
				}
				
				startActivity(intent);
				finish();//结束当前对象
			}
		});
    }

}
