package app.minimalvideoplayer.ui

import androidx.lifecycle.ViewModel
import app.minimalvideoplayer.analytics.DebugAnalytics

class MinimalVideoPlayerViewModel : ViewModel() {

    private val analytics = DebugAnalytics()

    fun trackPlaybackStarted() {
        analytics.trackPlaybackStarted()
    }

    fun trackPlaybackTimeAndSelectedTracks(
        positionSec: Long,
        selectedTracks: List<String>
    ) {
        analytics.trackPlaybackTimeAndSelectedTracks(positionSec, selectedTracks)
    }

    fun trackPlaybackBitrateUpdate(previousBitrate: Int, newBitrate: Int) {
        analytics.trackPlaybackBitrateUpdate(previousBitrate, newBitrate)
    }
}

