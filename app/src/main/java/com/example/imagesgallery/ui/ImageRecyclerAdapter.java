package com.example.imagesgallery.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.imagesgallery.R;
import com.example.imagesgallery.model.BitmapDto;
import com.example.imagesgallery.model.ImageDto;
import com.example.imagesgallery.service.Events;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageHolder> {
    private Context context;
    private List<ImageDto> dtoList = null;

    public ImageRecyclerAdapter(Context context) {
        this.context = context;
    }

    void clearData() {
        imageHolders.clear();
    }

    void setData(List<ImageDto> dtoList) {
        this.dtoList = dtoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);

        return new ImageRecyclerAdapter.ImageHolder(view);
    }

    private SparseArray<ImageHolder> imageHolders = new SparseArray<>();

    //set image to imageView
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBitmapLoad(@NonNull BitmapDto dto) {
        ImageHolder holder = imageHolders.get(dto.getPosition());
        //check current showing view(becauseof view recycle)
        if (holder == null || holder.position != dto.getPosition()) {
            return;
        }
        holder.imageView.setImageBitmap(dto.getBitmap());
        imageHolders.remove(dto.getPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, final int position) {
        final ImageDto item = dtoList.get(position);
        if (item == null) {
            return;
        }

        imageHolders.put(holder.getAdapterPosition(), holder);
        holder.position = holder.getAdapterPosition();
        holder.imageView.setImageResource(R.drawable.frodo);

        holder.imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                ImageDto target = ImageDto.builder()
                        .width(holder.imageView.getMeasuredWidth())
                        .height(holder.imageView.getMeasuredHeight())
                        .url(item.getUrl())
                        .linkUrl(item.getLinkUrl())
                        .position(holder.getAdapterPosition())
                        .build();
                Events.BUS.post(target);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtoList == null ? 0 : dtoList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_getty)
        ImageView imageView;
        int position;

        ImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.image_getty)
        void onClick() {
            int position = getLayoutPosition();
            ImageDto item = dtoList.get(position);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLinkUrl()));
            context.startActivity(intent);
        }

    }
}
