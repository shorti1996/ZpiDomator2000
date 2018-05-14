package zpi.pls.zpidominator2000.Api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public class ZpiApiRetrofitClient {

    public static final int HTTP_RESPONSE_OK = 200;

    public static final String apiServer = "192.168.0.106";
    public static String baseUrl = "http://" + apiServer + ":8000/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}