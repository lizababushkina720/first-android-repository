package com.example.hw_04.manager


object CoroutineKeys {
    const val NETWORK_EXCEPTION = "error_network"
    const val TIMEOUT_EXCEPTION = "error_timeout"
    const val DATABASE_EXCEPTION = "error_database"
    const val UNKNOWN_ERROR = "error_unknown"


    const val TOAST_CANCELLED= "msg_coroutines_cancelled"
    const val PROGRESS_LABEL = "progress_label"
    const val START_BUTTON = "btn_start"
    const val CANCEL_BUTTON = "btn_cancel"
    const val SLIDER_LABEL = "slider_coroutines"
    const val DISPATCHER_LABEL = "dispatcher_label"
    const val SEQUENTIAL_LABEL = "sequential_mode"
    const val PARALLEL_LABEL = "parallel_mode"
    const val LAZY_LABEL = "lazy_execution"
    const val BACKGROUND_LABEL = "background_work"
}
