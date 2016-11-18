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
 * ����ҳ��
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
        
        //��ת����
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//����ʱ��
        rotateAnimation.setFillAfter(true);//���ֶ����Ľ���״̬
        //���Ŷ���
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //���䶯��
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        
        //��������
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //��������
        rlRoot.startAnimation(animationSet);
        //�����ļ���
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
				//������������
				//����ǵ�һ�ν��룬��ת����������ҳ
				//������ǵ�һ�ν��룬��ת��������
				boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
				if (isFirstEnter) {
					//��������ҳ
					intent = new Intent(getApplicationContext(), GuideActivity.class);
				} else {
					//��ת��������
					intent = new Intent(getApplicationContext(),MainActivity.class);
				}
				
				startActivity(intent);
				finish();//������ǰ����
			}
		});
    }

}
