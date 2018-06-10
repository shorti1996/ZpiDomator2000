package zpi.pls.zpidominator2000.Activities

import android.os.Bundle
import zpi.pls.zpidominator2000.R

/**
 * Activity that serves as a help screen
 */
class HelpActivity : AppCompatActivityWithBackButton() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
    }

}
