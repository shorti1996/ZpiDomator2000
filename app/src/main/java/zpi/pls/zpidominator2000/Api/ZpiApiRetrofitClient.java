package zpi.pls.zpidominator2000.Api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public class ZpiApiRetrofitClient {

    public static final int HTTP_RESPONSE_OK = 200;

//    public static final String apiServer = "192.168.0.106";
//    public static final String apiServer = "192.168.1.202";
    public static String apiServer = "192.168.40.66";
//    public static String baseUrl = buildBaseUrl(apiServer);

    @NonNull
    private static String buildBaseUrl(String api) {
        return "http://" + api + ":8000/api/";
    }

    private static Retrofit retrofit = null;
    public static List<OnApiAddressChangedListener> callbacks = new LinkedList<>();

    private static Retrofit getRetrofitHelper(String apiAddress) {
        retrofit = new Retrofit.Builder()
                .baseUrl(buildBaseUrl(apiAddress))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ZpiApiRetrofitClient(@Nullable String apiAddress, OnApiAddressChangedListener callback) {
        if (apiAddress == null) {
            apiAddress = apiServer;
        }
        retrofit = getRetrofitHelper(apiAddress);
        callbacks.add(callback);
    }

    public static void changeApiServer(String newServerAddress) {
//        apiServer = newServerAddress;
        retrofit = getRetrofitHelper(newServerAddress);
        for (OnApiAddressChangedListener listener : callbacks) {
            listener.OnApiAddressChanged();
        }
    }

    public interface OnApiAddressChangedListener {
        void OnApiAddressChanged();
        }
}