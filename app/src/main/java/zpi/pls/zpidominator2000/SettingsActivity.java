package zpi.pls.zpidominator2000;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;

import zpi.pls.AppCompatActivityWithBackButton;
import zpi.pls.zpidominator2000.Api.ZpiApiRetrofitClient;

public class SettingsActivity extends AppCompatActivityWithBackButton {

    public EditText apiAddressVal;
    public Button apiAddressOk;
    public Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        apiAddressVal = findViewById(R.id.settings_api_address_value);
        loadApiAddressToView();
        apiAddressOk = findViewById(R.id.settings_api_address_ok);
        RxView.clicks(apiAddressOk)
                .subscribe(o -> {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String newAddress = String.valueOf(apiAddressVal.getText());
                    editor.putString(getString(R.string.setting_api_address_key), newAddress);
                    editor.commit();
                    ZpiApiRetrofitClient.changeApiServer(newAddress);
                    restartApp();
                });
        logoutBtn = findViewById(R.id.settings_user_logout_btn);
        RxView.clicks(logoutBtn)
                .subscribe(o -> {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(getString(R.string.saved_username_key));
                    editor.remove(getString(R.string.saved_password_key));
                    editor.commit();
                    ZpiApiRetrofitClient.clear();
                    restartApp();
                });
    }

    private void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            Utils.showToast(this, "Couldn't restart the app");
        }
//        Intent startIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
////        Intent startIntent = new Intent(this, MainActivity.class);
//        int mPendingIntentId = 123456;
//        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, mPendingIntent);
//        System.exit(0);
    }

    private void loadApiAddressToView() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.setting_api_address_default);
        String apiAddress = sharedPref.getString(getString(R.string.setting_api_address_key), defaultValue);
        apiAddressVal.setText(apiAddress);
    }

}
