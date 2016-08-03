package com.example.zkw.janggwa.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zkw.janggwa.R;
import com.example.zkw.janggwa.model.GanHuo;
import com.example.zkw.janggwa.retrofit.RetrofitHttpMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by zkw on 2016/7/7.
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.welcome_image)
    ImageView welcomeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_layout);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Subscriber subscriber = new Subscriber<GanHuo>() {

            @Override
            public void onCompleted() {
                Log.e("completed","completed");
                animateImage();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("666","onError");
                Glide.with(SplashActivity.this)
                        .load(R.drawable.wall_picture)
                        .into(welcomeImage);
                animateImage();
            }

            @Override
            public void onNext(GanHuo ganHuo) {
                Log.e("next","next");
                Glide.with(SplashActivity.this)
                        .load(ganHuo.getResults().get(0).getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(welcomeImage);
            }
        };
        RetrofitHttpMethod.getInstance().getGanHuo(subscriber, "福利", 1, 1);
    }

    private void animateImage() {
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2500);
        welcomeImage.startAnimation(scaleAnimation);

        //缩放动画监听
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this,MainActivity.class));

                overridePendingTransition(0,0);

                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
