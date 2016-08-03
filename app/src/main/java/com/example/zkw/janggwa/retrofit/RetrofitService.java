package com.example.zkw.janggwa.retrofit;


import com.example.zkw.janggwa.model.GanHuo;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by wd on 2016/7/7.
 */
public interface RetrofitService {
    @GET("api/data/{type}/{count}/{page}")
    rx.Observable<GanHuo> getGanHuo(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
}
