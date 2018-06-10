package zpi.pls.zpidominator2000.Activities;

import android.os.Bundle;

import zpi.pls.zpidominator2000.R;
import zpi.pls.zpidominator2000.Utils;

/**
 * Activity that shows info about the Application
 */
public class AboutActivity extends AppCompatActivityWithBackButton {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Utils.setupActionBar(this);
    }

}
