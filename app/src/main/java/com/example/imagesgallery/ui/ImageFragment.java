package com.example.imagesgallery.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.imagesgallery.R;
import com.example.imagesgallery.di.DaggerImageGalleryComponent;
import com.example.imagesgallery.di.ImageGalleryModule;
import com.example.imagesgallery.model.ImageDto;
import com.example.imagesgallery.service.Events;
import com.example.imagesgallery.service.image.ImageService;
import com.example.imagesgallery.service.image.cache.CompositeImageCache;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragment extends Fragment {

    private static final int SPAN_COUNT = 2;

    @Inject
    ImageRecyclerAdapter adapter;

    @Inject
    ImageService imageService;

    @Inject
    CompositeImageCache cache;

    @BindView(R.id.recyclerView)
    protected ImageRecyclerView recyclerView;
    private ProgressBarDialog progressBarDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerImageGalleryComponent.builder()
                .imageGalleryModule(new ImageGalleryModule(getContext()))
                .build().inject(this);

        setHasOptionsMenu(true);
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.BUS.regist(this);
        Events.BUS.regist(adapter);
        Events.BUS.regist(imageService);
    }

    @Override
    public void onStop() {
        super.onStop();
        Events.BUS.unregist(imageService);
        Events.BUS.unregist(adapter);
        Events.BUS.unregist(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.clearData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        recyclerView.initAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private void loadData() {
        showProgress(true);
        imageService.requestImages();
    }

    //set image List from site
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageList(List<ImageDto> images) {
        if(images.isEmpty()) {
            Toast.makeText(getContext(),
                    getString(R.string.msg_page_parsing_not_correct),
                    Toast.LENGTH_SHORT)
                    .show();
        }

        showProgress(false);
        recyclerView.setData(images);
    }

    private void showProgress(boolean isVisible) {
        if (isVisible) {
            progressBarDialog = new ProgressBarDialog(getActivity());
            progressBarDialog.show();
        } else {
            progressBarDialog.dismiss();
        }
    }
}
