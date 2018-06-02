package zpi.pls.zpidominator2000;

import android.os.Bundle;

import zpi.pls.AppCompatActivityWithBackButton;

public class AboutActivity extends AppCompatActivityWithBackButton {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Utils.setupActionBar(this);
    }

}
