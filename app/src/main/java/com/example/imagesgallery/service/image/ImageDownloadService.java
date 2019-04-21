package com.example.imagesgallery.service.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.example.imagesgallery.model.BitmapDto;
import com.example.imagesgallery.model.ImageDto;
import com.example.imagesgallery.service.Events;
import com.example.imagesgallery.service.image.cache.CompositeImageCache;
import com.example.imagesgallery.service.network.HttpImageApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDownloadService {
    private HttpImageApi httpImageApi = HttpImageApi.retrofit.create(HttpImageApi.class);
    private CompositeImageCache cache;

    public ImageDownloadService(CompositeImageCache cache) {
        this.cache = cache;
    }

    void download(ImageDto item) {
        Call<ResponseBody> call = httpImageApi.getImage(item.getUrl());
        call.enqueue(new DownloadCallback(item));
    }

    class DownloadCallback implements Callback<ResponseBody> {
        ImageDto item;

        DownloadCallback(ImageDto item) {
            this.item = item;
        }

        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
            Bitmap bitmap = getBitmapFromInputStream(response.body().byteStream(), item.width, item.height);
            if (bitmap != null) {
                cache.setAsync(item.getUrl(), bitmap); //use AsyncTask for Caching
                BitmapDto target = BitmapDto.builder()
                        .position(item.getPosition())
                        .url(item.getUrl())
                        .bitmap(bitmap)
                        .build();

                Events.BUS.get().post(target);
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        }
    }

    private Bitmap getBitmapFromInputStream(InputStream stream, int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();

            //stream reset이 필요한데, 이미지라서 메모리에 큰 영향이 없어서 ByteArrayInputStream 사용. 따라서 stream close 생략함
            InputStream markSupportStream = new ByteArrayInputStream(baos.toByteArray());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(markSupportStream, null, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            markSupportStream.reset();
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(markSupportStream, null, options);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 리사이징 부분은 구글 개발자 사이트 참고
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
