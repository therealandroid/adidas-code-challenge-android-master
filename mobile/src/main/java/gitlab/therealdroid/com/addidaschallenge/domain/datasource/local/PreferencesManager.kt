package gitlab.therealdroid.com.addidaschallenge.domain.datasource.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(var context: Context) {

    private var sharedPreferences: SharedPreferences? = null

    object Prefs {
        const val PREF_ATTR_STEP_COUNT = "stepCount"
        const val PREF_ATTR_TIP_DIALOG = "tip"
        const val PREF_NAME = "adidas"
    }

    init {
        sharedPreferences = context.getSharedPreferences(Prefs.PREF_NAME, Context.MODE_PRIVATE)
    }

    fun save(prefAttr: String, value: Int) {
        sharedPreferences!!.edit()
            .putInt(prefAttr, value)
            .apply()
    }

    fun get(prefAttr: String): Int {
        return sharedPreferences!!.getInt(prefAttr, 0)
    }
}