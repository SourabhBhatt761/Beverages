package com.srb.beverages

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import com.srb.beverages.utils.timber.DebugTree
import com.srb.beverages.utils.timber.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//begins
@HiltAndroidApp
class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        setDebugTools()
    }
    private fun setDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

}



