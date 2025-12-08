package com.example.hw_04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.hw_04.manager.CoroutineStateManager
import com.example.hw_04.model.CoroutineException
import com.example.hw_04.model.CoroutineSettings
import com.example.hw_04.ui.screens.CoroutinesScreen
import com.example.hw_04.ui.theme.HW_04Theme

class MainActivity : ComponentActivity() {

    private var stateManager: CoroutineStateManager? = null
    private var currentSettings: CoroutineSettings? = null
    private var backgroundWorkEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HW_04Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    val uiScope = rememberCoroutineScope()

                    val stateManagerInstance = remember { CoroutineStateManager() }
                    stateManager = stateManagerInstance

                    var isLoading by remember { mutableStateOf(false) }
                    var completed by remember { mutableStateOf(0) }
                    var total by remember { mutableStateOf(0) }
                    var lastToastMessage by remember { mutableStateOf<String?>(null) }

                    stateManagerInstance.onProgressUpdate = { done, all ->
                        completed = done
                        total = all
                    }

                    stateManagerInstance.onCoroutineException = { exception ->
                        lastToastMessage = when (exception) {
                            is CoroutineException.NetworkException ->
                                context.getString(R.string.error_network)
                            is CoroutineException.TimeoutException ->
                                context.getString(R.string.error_timeout)
                            is CoroutineException.DatabaseException ->
                                context.getString(R.string.error_database)
                        }
                    }

                    stateManagerInstance.onAllCompleted = { results ->
                        isLoading = false
                        val success = results.count { it.isSuccess }
                        val failed = results.size - success
                        lastToastMessage = context.getString(
                            R.string.msg_coroutines_result,
                            success,
                            failed
                        )
                    }


                    stateManagerInstance.onCancelled = { cancelledCount ->
                        isLoading = false
                        lastToastMessage = context.getString(
                            R.string.msg_coroutines_cancelled,
                            cancelledCount
                        )
                    }



                    lastToastMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        lastToastMessage = null
                    }
                    CoroutinesScreen(
                        isLoading = isLoading,
                        completed = completed,
                        total = total,
                        onStartClicked = { uiSettings ->
                            val settings = CoroutineSettings(
                                count = uiSettings.count,
                                dispatcher = uiSettings.dispatcher,
                                isSequential = uiSettings.isSequential,
                                isParallel = uiSettings.isParallel,
                                isLazy = uiSettings.isLazy,
                                isBackgroundWork = uiSettings.isBackgroundWork
                            )
                            currentSettings = settings
                            backgroundWorkEnabled = uiSettings.isBackgroundWork

                            isLoading = true
                            completed = 0
                            total = uiSettings.count

                            uiScope.launch {
                                stateManagerInstance.startCoroutines(
                                    scope = this,
                                    settings = settings
                                )
                            }
                        },
                        onCancelClicked = {
                            stateManagerInstance.cancelAllCoroutines()
                        },
                        onResetRequested = {
                        }
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val stateManager = stateManager ?: return

        if (!backgroundWorkEnabled) {
            val cancelledCount = stateManager.cancelAllCoroutines()
            stateManager.setLastCancelledCount(cancelledCount)
        }
    }
    override fun onResume() {
        super.onResume()
        val stateManager = stateManager ?: return

        if (!backgroundWorkEnabled) {
            val cancelledCount = stateManager.getLastCancelledCount()
            val baseSettings = currentSettings
            if (cancelledCount > 0 && baseSettings != null) {
                val restartSettings = baseSettings.copy(count = cancelledCount)
                val scope = kotlinx.coroutines.CoroutineScope(restartSettings.dispatcher)

                scope.launch {
                    stateManager.startCoroutines(
                        scope = this,
                        settings = restartSettings
                    )
                }
                stateManager.setLastCancelledCount(0)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        stateManager = null
    }
}

