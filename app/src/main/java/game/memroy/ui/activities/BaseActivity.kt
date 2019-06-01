package game.memroy.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {


    abstract val layout: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
