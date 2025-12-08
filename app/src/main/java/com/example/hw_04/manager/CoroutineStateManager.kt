package com.example.hw_04.manager

import com.example.hw_04.model.CoroutineException
import com.example.hw_04.model.CoroutineResult
import com.example.hw_04.model.CoroutineSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoroutineStateManager {
    private val activeJobs = mutableListOf<Job>()
    private val results = mutableListOf<CoroutineResult>()
    private var lastSettings: CoroutineSettings? = null
    private var lastCancelledCount = 0
    var onProgressUpdate: ((completed: Int, total: Int) -> Unit)? = null
    var onCoroutineException: ((exception: CoroutineException) -> Unit)? = null
    var onAllCompleted: ((results: List<CoroutineResult>) -> Unit)? = null
    var onCancelled: ((cancelledCount: Int) -> Unit)? = null
    fun startCoroutines(scope: CoroutineScope, settings: CoroutineSettings) {
        lastSettings = settings
        results.clear()
        activeJobs.clear()

        scope.launch {
            if (settings.isLazy) {
                delay(3000)
            }

            if (settings.isSequential) {
                executeSequential(this, settings)
            } else if (settings.isParallel) {
                executeParallel(this, settings)
            }
        }
    }

    private suspend fun executeSequential(scope: CoroutineScope, settings: CoroutineSettings) {
        repeat(settings.count) { index ->
            val job = scope.launch(settings.dispatcher) {
                executeHeavyOperation(index, settings)
            }

            activeJobs.add(job)
            job.join()
            onProgressUpdate?.invoke(index + 1, settings.count)
        }

        onAllCompleted?.invoke(results.toList())
    }
    private suspend fun executeParallel(scope: CoroutineScope, settings: CoroutineSettings) {

        val deferredList = mutableListOf<kotlinx.coroutines.Deferred<Unit>>()

        repeat(settings.count) { index ->
            val deferred = scope.async(settings.dispatcher) {
                executeHeavyOperation(index, settings)
            }
            deferredList.add(deferred)
            activeJobs.add(deferred)
        }

        deferredList.forEachIndexed { index, deferred ->
            try {
                deferred.await()
                onProgressUpdate?.invoke(index + 1, settings.count)
            } catch (e: Exception) {
                onProgressUpdate?.invoke(index + 1, settings.count)
            }
        }

        onAllCompleted?.invoke(results.toList())
    }


    private suspend fun executeHeavyOperation(id: Int, settings: CoroutineSettings) {
        val startTime = System.currentTimeMillis()
        val delayTime = Random.nextLong(1000, 10001)
        delay(delayTime)

        val executionTime = System.currentTimeMillis() - startTime
        if (executionTime >= 7000 && Random.nextDouble() < 0.3) {
            val exception = generateRandomException()
            onCoroutineException?.invoke(exception)

            results.add(
                CoroutineResult(
                    id = id,
                    executionTime = executionTime,
                    isSuccess = false,
                    errorMessage = exception.message ?: CoroutineKeys.UNKNOWN_ERROR
                )
            )
            return
        }
        results.add(
            CoroutineResult(
                id = id,
                executionTime = executionTime,
                isSuccess = true
            )
        )
    }

    private fun generateRandomException(): CoroutineException {
        val exceptions = listOf(
            CoroutineException.NetworkException(CoroutineKeys.NETWORK_EXCEPTION),
            CoroutineException.TimeoutException(CoroutineKeys.TIMEOUT_EXCEPTION),
            CoroutineException.DatabaseException(CoroutineKeys.DATABASE_EXCEPTION)
        )
        return exceptions.random()
    }

    fun cancelAllCoroutines(): Int {

        val cancelledCount = activeJobs.size
        lastCancelledCount = cancelledCount
        activeJobs.forEach { job ->
            job.cancel()
        }
        activeJobs.clear()
        onCancelled?.invoke(cancelledCount)
        return cancelledCount
    }
    fun getLastCancelledCount(): Int = lastCancelledCount

    fun setLastCancelledCount(count: Int) {
        lastCancelledCount = count
    }
    fun getLastSettings(): CoroutineSettings? = lastSettings

    fun getResults(): List<CoroutineResult> = results.toList()

    fun reset() {
        activeJobs.clear()
        results.clear()
        lastCancelledCount = 0
    }
}

