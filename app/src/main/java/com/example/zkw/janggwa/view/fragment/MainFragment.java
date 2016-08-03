package com.example.zkw.janggwa.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.zkw.janggwa.R;
import com.example.zkw.janggwa.adapter.GanHuoAdapter;
import com.example.zkw.janggwa.adapter.MeiZhiAdapter;
import com.example.zkw.janggwa.model.GanHuo;
import com.example.zkw.janggwa.retrofit.RetrofitHttpMethod;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.http.HttpMethod;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zkw on 2016/7/7.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @BindView(R.id.no_network)
    LinearLayout noNetwork;
    @BindView(R.id.recycler_view)
    EasyRecyclerView recyclerView;
    private String title;
    private List<GanHuo.Result> ganhuoList;
    private GanHuoAdapter ganHuoAdapter;
    private MeiZhiAdapter meiZhiAdapter;
    private int page = 1;
    private Handler handler = new Handler();

    public static MainFragment getInstance(String title) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        ganhuoList = new ArrayList<>();
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recycler_view);

        if (title.equals("福利")){
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            meiZhiAdapter = new MeiZhiAdapter(getContext());

            dealWithAdapter(meiZhiAdapter);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ganHuoAdapter = new GanHuoAdapter(getContext());
            //recyclerView.setAdapterWithProgress(ganHuoAdapter);
            dealWithAdapter(ganHuoAdapter);
        }

        recyclerView.setRefreshListener(this);
        onRefresh();
    }

    private void dealWithAdapter(final RecyclerArrayAdapter<GanHuo.Result> adapter) {
        recyclerView.setAdapterWithProgress(adapter);

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Snackbar.make(recyclerView,adapter.getItem(position).getDesc(), Snackbar.LENGTH_SHORT).show();
                if (title.equals("福利")){
//                    Intent intent = new Intent(getContext(), MeiZhiActivity.class);
//                    jumpActivity(intent,adapter,position);
                }else {
//                    Intent intent = new Intent(getContext(), GanHuoActivity.class);
//                    jumpActivity(intent,adapter,position);
                }
            }
        });
    }

    private void jumpActivity(Intent intent,RecyclerArrayAdapter<GanHuo.Result> adapter,int position) {
        intent.putExtra("desc",adapter.getItem(position).getDesc());
        intent.putExtra("url",adapter.getItem(position).getUrl());
        startActivity(intent);
    }

    private void getData(String type,int count,int page) {

                Subscriber subscriber = new Subscriber<GanHuo>() {
                    @Override
                    public void onCompleted() {
                        Log.e("666","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(recyclerView,"NO WIFI，不能愉快的看妹纸啦..",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(GanHuo ganHuo) {
                        ganhuoList = ganHuo.getResults();
                        if (title.equals("福利")){
                            meiZhiAdapter.addAll(ganhuoList);
                        }else {
                            ganHuoAdapter.addAll(ganhuoList);
                        }
                    }
                };
        RetrofitHttpMethod.getInstance().getGanHuo(subscriber,type,count,page);
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (title.equals("福利")){
                    meiZhiAdapter.clear();
                    getData("福利",20,1);
                }else{
                    ganHuoAdapter.clear();
                    if (title.equals("Android")){
                        getData("Android",20,1);
                    }else if (title.equals("iOS")){
                        getData("iOS",20,1);
                    }
                    else if (title.equals("休息视频")){
                        getData("休息视频",20,1);
                    }
                }
                page = 2;
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (title.equals("福利")){
                    getData("福利",20,page);
                }else if (title.equals("Android")){
                    getData("Android",20,page);
                }else if (title.equals("iOS")){
                    getData("iOS",20,page);
                }
                else if (title.equals("休息视频")){
                    getData("休息视频",20,page);
                }
                page++;
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
