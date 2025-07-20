package com.luciaaldana.eccomerceapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.luciaaldana.eccomerceapp.feature.productlist.worker.ProductSyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EccomerceApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var productSyncScheduler: ProductSyncScheduler

    override fun onCreate() {
        super.onCreate()
        productSyncScheduler.scheduleProductSync()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}