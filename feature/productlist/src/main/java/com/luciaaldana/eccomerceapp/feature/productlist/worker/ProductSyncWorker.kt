package com.luciaaldana.eccomerceapp.feature.productlist.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val productRepository: ProductRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val products = productRepository.syncProductsFromApi()
            
            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }
}