package com.srb.gourmet

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import com.srb.gourmet.utils.timber.DebugTree
import com.srb.gourmet.utils.timber.ReleaseTree
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



