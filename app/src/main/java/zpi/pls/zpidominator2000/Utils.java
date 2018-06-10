package zpi.pls.zpidominator2000;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wojciech.liebert on 14.05.2018.
 */

public class Utils {

    /**
     * Show toast with {@link  Toast#LENGTH_SHORT}
     * @param context needed to even touch the UI
     * @param text Text of the toast
     */
    public static void showToast(Context context, String text) {
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            Log.w("SHOWTOAST", "Failed because passed Context is null");
        }
    }

    public static void showToastOnUi(Context context, String text) {
        Observable.just(1)
                .doOnNext(x -> showToast(context, text))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .subscribe();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void setupActionBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            ActionBar supportActionBar = activity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

}
