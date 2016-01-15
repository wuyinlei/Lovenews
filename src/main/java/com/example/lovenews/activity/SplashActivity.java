package com.example.lovenews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.lovenews.R;
import com.example.lovenews.utils.PrefUtils;

public class SplashActivity extends BaseActivity {

    private RelativeLayout rl_root;
    private SharedPreferences mPres;
    private boolean showed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        startAnima();
        //jumpNextPage();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 闪屏页面开始动画
     */
    private void startAnima() {

        //动画集合
        AnimationSet set = new AnimationSet(false);

        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        //动画时间
        rotate.setDuration(2000);
        //保持旋转状态
        rotate.setFillAfter(true);
        set.addAnimation(rotate);

        //缩放动画
        ScaleAnimation scalf = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //动画时间
        scalf.setDuration(2000);
        //动画状态
        scalf.setFillAfter(true);
        set.addAnimation(scalf);

        //淡入淡出动画
        AlphaAnimation alph = new AlphaAnimation(0, 1);
        alph.setDuration(2000);
        alph.setFillAfter(true);
        set.addAnimation(alph);

        /**
         * 设置动画监听事件
         */
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * 动画结束的时候跳转到引导页
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rl_root.startAnimation(set);
    }

    private void jumpNextPage() {
        showed = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
        /*mPres = getSharedPreferences("config", MODE_PRIVATE);
        showed = mPres.getBoolean("is_user_guide_showed", false);*/
        if (!showed) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
