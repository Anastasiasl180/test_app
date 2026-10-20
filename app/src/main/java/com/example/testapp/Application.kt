package com.example.testapp

import android.app.Application
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDependencyGraph()
    }

    private fun initializeDependencyGraph() {
        Paper.init(this)
        startKoin {
            androidContext(this@Application)
            modules(AppModule().module)
        }
    }
}
