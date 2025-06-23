package com.vb.footballmatchviewer

import android.app.Application
import com.vb.footballmatchviewer.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class FootballApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FootballApp)
            androidLogger(level = Level.ERROR)
            modules(mainModule)
        }
    }
}
