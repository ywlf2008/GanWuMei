package com.dimon.ganwumei.features.newsfeed.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.api.FileStore;
import com.dimon.ganwumei.api.thread.ThreadPoolManager;
import com.dimon.ganwumei.features.UiHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dimon on 2016/3/24.
 */
public class AppStart extends BaseActivity {

    private static final String TAG = "AppStart";
    private static final long LOADING_TIME = 5000;
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_ENTER_HOME = 4;
    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    Animation scaleAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        ButterKnife.bind(this);
        startAnima();

        ThreadPoolManager.execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                //准备工作
                initApp();

                Looper.loop();
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, LOADING_TIME);
    }

    private void startAnima() {
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.7f, 0.0f, 1.7f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(8500);
        animation.setFillAfter(true);
        ivSplash.startAnimation(animation);

//        scaleAnimation = new ScaleAnimation(0.0f,1.4f,0.0f,1.4f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        scaleAnimation.setInterpolator(new OvershootInterpolator());
//        ivSplash.startAnimation(scaleAnimation);
    }

    /** 初始化工作 */
    private void initApp() {
        //10M的volley缓存
        //VolleyManager.INSTANCE.initQueue((10 << 10) << 10);
        FileStore.INSTANCE.createFileFolder();
    }

    /** 启动 */
    private void goHome() {
        UiHelper.startToMainActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivSplash.clearAnimation();
        ivSplash = null;
    }
}
