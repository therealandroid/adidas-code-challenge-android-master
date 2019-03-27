package gitlab.therealdroid.com.addidaschallenge.configuration.device

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat


fun Context.hasInternetConnection(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}


fun Activity.showSnack(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <T : Activity> Activity.openActivity(activity: Class<T>, extras: Bundle? = null) {
    val intent = Intent(this, activity)

    if (extras != null) {
        intent.putExtras(extras)
    }

    startActivity(intent)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> Math.log10(Math.abs(toDouble())).toInt() + 1
}

fun Int.format(): String {
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this)
}