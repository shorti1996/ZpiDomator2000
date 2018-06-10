package zpi.pls.zpidominator2000.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zpi.pls.zpidominator2000.R;

/**
 * Created by wojciech.liebert on 13.05.2018.
 */

public class ZpiApiRetrofitClient {

    public static final int HTTP_RESPONSE_OK = 200;

    private static String apiServer = "212.237.52.192";
    private static Retrofit retrofit = null;
    private static Request authorization;

    @NonNull
    private static String buildBaseUrl(String api) {
        return "http://" + api + "/api/";
    }
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static List<OnApiAddressChangedListener> callbacks = new LinkedList<>();

    public static void clear() {
        retrofit = null;
        authorization = null;
    }

    private static Retrofit getRetrofitHelper(String apiAddress, @Nullable String username, @Nullable String password) {
        if (authorization == null) {
            if (username != null && password != null) {
                // TODO It destroys the whole idea of authentication
                // Possible easy fix: store this Request instead of static field "authorization"
                // I don't have the time right now
//                httpClient.addInterceptor(chain -> {
//                    Request request = chain.request().newBuilder()
//                            .addHeader("Authorization", Credentials.basic(username, password))
//                            .build();
//                    return chain.proceed(request);
//                });
                httpClient.authenticator((route, response) -> {
                    authorization = response.request().newBuilder()
                            .header("Authorization", Credentials.basic(username, password))
                            .build();
                    return authorization;
                });
            } else {
                throw new IllegalStateException("No authorization set before and no username and password provided now");
            }
        }
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

    public ZpiApiRetrofitClient(@Nullable String apiAddress, String username, String password, OnApiAddressChangedListener callback) {
        if (apiAddress == null) {
            apiAddress = apiServer;
        }
        retrofit = getRetrofitHelper(apiAddress, username, password);
        Log.d("AA", retrofit.baseUrl().toString());
        if (callback != null) {
            callbacks.add(callback);
        }
    }

    public static void changeApiServer(String newServerAddress, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.setting_api_address_key), newServerAddress);
        editor.commit();
        retrofit = getRetrofitHelper(newServerAddress, null, null);
        for (OnApiAddressChangedListener listener : callbacks) {
            listener.OnApiAddressChanged(newServerAddress);
        }
    }

    public interface OnApiAddressChangedListener {
        void OnApiAddressChanged(String newAddress);
    }
}