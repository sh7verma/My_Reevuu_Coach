package com.myreevuuCoach.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://52.52.111.12:3000";/// Applify Development

    public static final String URL_TERMS_CONDITIONS = "http://52.52.111.12:3000/terms-and-conditions";
    public static final String URL_PRIVACY_POLICY = "http://52.52.111.12:3000/privacy-policy";
    public static final String URL_ABOUT_US = "http://52.52.111.12:3000/privacy-policy";
    public static final String URL_FAQs = "http://52.52.111.12:3000/privacy-policy";
    public static final String URL_REVIEW_VIDEO = "http://52.52.111.12:3000/coach/mobile_reviews_video/";
    public static final String URL_PREVIEW_VIDEO = "http://52.52.111.12:3000/coach/mobile_preview_video/";


    public static Retrofit retrofit = null;
    private static ApiInterface apiInterface = null;

    public static ApiInterface getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (apiInterface == null) {
            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }

    //Creating OKHttpClient
    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(100000, TimeUnit.SECONDS)
                .connectTimeout(100000, TimeUnit.SECONDS)
                .build();
    }
}
