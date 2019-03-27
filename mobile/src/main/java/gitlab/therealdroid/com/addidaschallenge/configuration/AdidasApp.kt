package gitlab.therealdroid.com.addidaschallenge.configuration

import android.app.Application
import gitlab.therealdroid.com.addidaschallenge.BuildConfig
import io.realm.Realm
import io.realm.RealmConfiguration


class AdidasApp: Application(){

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val config : RealmConfiguration = if(BuildConfig.DEBUG){
            RealmConfiguration.Builder()
                .name(ProjectConfiguration.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(ProjectConfiguration.DB_VERSION)
                .build()

        }else{
            RealmConfiguration.Builder()
                .name(ProjectConfiguration.DB_NAME)
                .schemaVersion(ProjectConfiguration.DB_VERSION)
                .build()

        }

        Realm.setDefaultConfiguration(config)

    }
}