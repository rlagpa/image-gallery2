package com.example.imagesgallery.service.image;

import android.support.annotation.NonNull;

import com.example.imagesgallery.config.Constants;
import com.example.imagesgallery.model.ImageDto;
import com.example.imagesgallery.service.Events;
import com.example.imagesgallery.service.network.HttpImageApi;
import com.example.imagesgallery.service.parser.RootHtmlParser;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO dagger
/**
 * get image list from site address
 */
public class ImageListService implements Callback<ResponseBody> {

    private HttpImageApi httpImageApi = HttpImageApi.retrofit.create(HttpImageApi.class);

    void request() {
        final Call<ResponseBody> call = httpImageApi.getImageList();
        call.enqueue(this);
    }

    private void post(List<ImageDto> images) {
        Events.BUS.get().post(images);
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        if (response.body() == null) {
            return;
        }

        RootHtmlParser imageParser = Constants.CURRENT_TYPE.getParser();
        List<ImageDto> imageList = imageParser.parse(response.body().byteStream(), Constants.CURRENT_TYPE.getBaseUrl());
        post(imageList);//image download url list 결과 전송
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        post(Collections.emptyList());//실패일 경우 empty list 전송
    }
}
