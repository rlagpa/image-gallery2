package com.example.imagesgallery.service.network;

import com.example.imagesgallery.config.Constants;

import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * use retrofit http client library request information
 */
public interface HttpImageApi {
    @GET("collection/sasha")
    Call<ResponseBody> getImageList();

    @GET
    Call<ResponseBody> getImage(@Url String imageUrl);

  /*  static Dispatcher dispatcher() {
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(10));
        dispatcher.setMaxRequestsPerHost(10);
        return dispatcher;
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .dispatcher(dispatcher())
            .build();
*/

    Retrofit retrofit = new Retrofit.Builder()
//            .client(client)
            .callbackExecutor(Executors.newFixedThreadPool(Constants.API_CALLBACK_THREADPOOL_SIZE))
            .baseUrl(Constants.CURRENT_TYPE.getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
}
