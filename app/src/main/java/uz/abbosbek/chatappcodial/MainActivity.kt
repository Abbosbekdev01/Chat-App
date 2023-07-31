package uz.abbosbek.chatappcodial

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.abbosbek.chatappcodial.utils.MyData.USER
import uz.abbosbek.chatappcodial.utils.MyData.screenLengthItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenLengthItem = (getScreenWidthInPixels(this) * 0.6).toInt()
    }
    override fun onStop() {
        super.onStop()
        CoroutineScope(Dispatchers.IO).launch {
            USER.statusTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            USER.isOnline = "false"
            FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().uid!!).setValue(USER)
        }
    }

    private fun getScreenWidthInPixels(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
}