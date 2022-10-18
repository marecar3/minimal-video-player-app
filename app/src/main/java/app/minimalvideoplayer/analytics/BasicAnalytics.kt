package app.minimalvideoplayer.analytics

abstract class BasicAnalytics {

    fun trackPlaybackStarted() {
        trackEvent(PLAYBACK_STARTED_EVENT)
    }

    fun trackPlaybackTimeAndSelectedTracks(positionSec: Long, selectedTracks: List<SelectedTrack>) {
        val params =
            mutableMapOf(
                PLAYBACK_POSITION_SEC_PARAM to positionSec,
                PLAYBACK_SELECTED_TRACKS_PARAM to selectedTracks
            )
        trackEvent(PLAYBACK_POSITION_AND_SELECTED_TRACKS_EVENT, params)
    }

    fun trackPlaybackBitrateUpdate(previousBitrate: Long, newBitrate: Long) {
        val params =
            mutableMapOf(
                PLAYBACK_BITRATE_PREVIOUS_PARAM to previousBitrate,
                PLAYBACK_BITRATE_NEW_PARAM to newBitrate
            )
        trackEvent(PLAYBACK_POSITION_AND_SELECTED_TRACKS_EVENT, params)
    }

    abstract fun trackEvent(eventName: String, params: Map<String, Any>? = null)

    companion object {
        const val PLAYBACK_STARTED_EVENT = "PLAYBACK_STARTED_EVENT"
        const val PLAYBACK_POSITION_AND_SELECTED_TRACKS_EVENT = "PLAYBACK_TIME_AND_SELECTED_TRACKS"

        const val PLAYBACK_POSITION_SEC_PARAM = "position_sec"
        const val PLAYBACK_SELECTED_TRACKS_PARAM = "selected_tracks"

        const val PLAYBACK_BITRATE_PREVIOUS_PARAM = "bitrate_previous"
        const val PLAYBACK_BITRATE_NEW_PARAM = "bitrate_new"
    }

    data class SelectedTrack(val type: String, val mimeType: String, val resolution: String)

}
