package zpi.pls.zpidominator2000;

import android.content.Context;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wojciech.liebert on 14.05.2018.
 */

public class Utils {

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastOnUi(Context context, String text) {
        Observable.just(1)
                .doOnNext(x -> showToast(context, text))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.computation())
                .subscribe();
    }

}
