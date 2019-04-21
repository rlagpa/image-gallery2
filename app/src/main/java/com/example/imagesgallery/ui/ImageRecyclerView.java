package com.example.imagesgallery.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.imagesgallery.model.ImageDto;

import java.util.List;

public class ImageRecyclerView extends RecyclerView {
   private ImageRecyclerAdapter adapter;


    public ImageRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public ImageRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ImageRecyclerView(Context context) {
        super(context);
    }

    public void setData(List<ImageDto> dtoList) {
        adapter.setData(dtoList);
    }

    public void initAdapter(ImageRecyclerAdapter adapter) {
        this.adapter = adapter;
        setAdapter(this.adapter);
    }

    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }
}

