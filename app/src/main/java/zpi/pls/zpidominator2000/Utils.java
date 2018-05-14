package zpi.pls.zpidominator2000;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wojciech.liebert on 14.05.2018.
 */

public class Utils {

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
