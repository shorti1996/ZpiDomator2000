package zpi.pls.zpidominator2000

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
    }

    fun toastMe(view: View) {
        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show();
    }
}
