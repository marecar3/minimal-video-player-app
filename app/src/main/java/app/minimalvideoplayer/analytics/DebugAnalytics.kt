package app.minimalvideoplayer.analytics

import android.util.Log

class DebugAnalytics : BasicAnalytics() {

    override fun trackEvent(eventName: String, params: Map<String, Any>?) {
        Log.d(
            TAG, if (params.isNullOrEmpty()) {
                "event name: $eventName"
            } else {
                "event name: $eventName, params: $params"
            }
        )
    }
}

const val TAG = "DEBUG"