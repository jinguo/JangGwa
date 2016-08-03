package com.example.zkw.janggwa.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.zkw.janggwa.model.GanHuo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class GanHuoAdapter extends RecyclerArrayAdapter<GanHuo.Result> {

    public GanHuoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GanHuoViewHolder(parent);
    }
}
