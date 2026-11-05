package com.blocker.engine.wordfilter.logging

import android.util.Log

class WordEntryDetectionLogger(
    private val enabled: Boolean = true
) {

    companion object {
        private const val TAG = "Blocker2WordFilter"
    }

    fun detection(
        packageName: String,
        matchedWord: String
    ) {
        if (!enabled) return

        Log.i(
            TAG,
            "Word detected: package=$packageName, configuredWord=$matchedWord"
        )
    }

    fun processingError(
        packageName: String,
        error: Throwable
    ) {
        if (!enabled) return

        Log.e(
            TAG,
            "Detection processing failed for package=$packageName",
            error
        )
    }

    fun serviceStatus(message: String) {
        if (!enabled) return

        Log.d(TAG, message)
    }
}
