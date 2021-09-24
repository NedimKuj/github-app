package com.nedkuj.github

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        setupRealm()
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private fun setupRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        )
    }
}