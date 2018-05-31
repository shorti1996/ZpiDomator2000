package zpi.pls.zpidominator2000.Api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public class ZpiApiRetrofitClient {

    public static final int HTTP_RESPONSE_OK = 200;

    @NonNull
    private static String buildBaseUrl(String api) {
        return "http://" + api + "/api/";
    }

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static List<OnApiAddressChangedListener> callbacks = new LinkedList<>();

    private static Retrofit getRetrofitHelper(String apiAddress) {
        httpClient.authenticator((route, response) ->
                response.request().newBuilder()
                        .header("Authorization",
                                Credentials.basic("User1", "UserPassword1"))
                        .build());
        retrofit = new Retrofit.Builder()
                .baseUrl(buildBaseUrl(apiAddress))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ZpiApiRetrofitClient(@Nullable String apiAddress, OnApiAddressChangedListener callback) {
        if (apiAddress == null) {
            String apiServer = "212.237.52.192";
            apiAddress = apiServer;
        }
        retrofit = getRetrofitHelper(apiAddress);
        Log.d("AA", retrofit.baseUrl().toString());
        callbacks.add(callback);
    }

    public static void changeApiServer(String newServerAddress) {
        retrofit = getRetrofitHelper(newServerAddress);
        for (OnApiAddressChangedListener listener : callbacks) {
            listener.OnApiAddressChanged();
        }
    }

    public interface OnApiAddressChangedListener {
        void OnApiAddressChanged();
        }
}