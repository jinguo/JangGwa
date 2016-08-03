package com.example.zkw.janggwa.retrofit;

import com.example.zkw.janggwa.model.GanHuo;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zkw on 2016/7/7.
 */
public class RetrofitHttpMethod {
    public static final String BASE_URL = "http://gank.io/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private RetrofitService retrofitService;

    private RetrofitHttpMethod() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
    }

    //在访问时创建单例
    private static class SingletonHolder{
        private static final RetrofitHttpMethod INSTANCE = new RetrofitHttpMethod();
    }

    //获取单例
    public static RetrofitHttpMethod getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getGanHuo(Subscriber<GanHuo> subscriber, String type, int count, int page){
        retrofitService.getGanHuo(type, count, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
