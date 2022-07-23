package com.example.dataflow.webService;
import com.example.dataflow.App;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
    private ServiceGenerator() {
    }

    public static <S> S newService(Class<S> serviceClass,String baseUrl){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(logging);
        clientBuilder.connectTimeout(50, TimeUnit.SECONDS);
        clientBuilder.readTimeout(50,TimeUnit.SECONDS);
        OkHttpClient client = clientBuilder.build();
        builder.baseUrl(baseUrl);
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        Retrofit adapter = builder.build();
        return adapter.create(serviceClass);
    }
    public static <S> S tokenService(Class<S> serviceClass,String baseUrl){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit.Builder builder = new Retrofit.Builder();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(logging);
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization", "Bearer "+ App.currentUser.getToken())
                        .addHeader("Accept", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        clientBuilder.connectTimeout(50, TimeUnit.SECONDS);
        clientBuilder.readTimeout(50,TimeUnit.SECONDS);
        OkHttpClient client = clientBuilder.build();
        builder.baseUrl(baseUrl);
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
        Retrofit adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
