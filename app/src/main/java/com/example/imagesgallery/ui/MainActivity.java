package com.example.imagesgallery.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.imagesgallery.R;
import com.example.imagesgallery.di.DaggerImageGalleryComponent;
import com.example.imagesgallery.di.ImageGalleryModule;
import com.example.imagesgallery.service.image.ImageService;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    ImageService imageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerImageGalleryComponent.builder()
                .imageGalleryModule(new ImageGalleryModule(getApplicationContext()))
                .build().inject(this);

        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ImageFragment fragment = new ImageFragment();
            transaction.replace(R.id.fragment_image, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
