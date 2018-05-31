package zpi.pls.zpidominator2000;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;

import zpi.pls.zpidominator2000.Api.ZpiApiRetrofitClient;

public class SettingsActivity extends AppCompatActivity {

    public EditText apiAddressVal;
    public Button apiAddressOk;

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
                    editor.apply();
                    ZpiApiRetrofitClient.changeApiServer(newAddress);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    if (i != null) {
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        Utils.showToast(this, "Couldn't update the address");
                    }
                });
    }

    private void loadApiAddressToView() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_prefs_name), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.setting_api_address_default);
        String apiAddress = sharedPref.getString(getString(R.string.setting_api_address_key), defaultValue);
        apiAddressVal.setText(apiAddress);
    }
}
