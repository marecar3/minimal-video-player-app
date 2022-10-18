package app.minimalvideoplayer.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.LoadEventInfo
import com.google.android.exoplayer2.source.MediaLoadData
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.ExoTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import java.util.concurrent.TimeUnit

class MinimalVideoPlayer(
    context: Context,
    val onPlaybackStarted: () -> Unit,
    val onPlaybackTimeAndSelectedTracksUpdate: (
        positionSec: Long,
        selectedTracks: List<String>
    ) -> Unit,
    val onBitrateUpdate: (previousBitrate: Int, newBitrate: Int) -> Unit
) {

    private var player: ExoPlayer
    private var previousBitrate: Int = -1
    private val runRecursiveFunction = false

    private val runnable = object : Runnable {

        override fun run() {
            sendTimeAndSelectedTracksUpdate()

            Handler(Looper.getMainLooper()).postDelayed(
                this, 1000
            )
        }
    }

    init {
        val videoTrackSelectionFactory: ExoTrackSelection.Factory = AdaptiveTrackSelection.Factory()
        val trackSelector: TrackSelector = DefaultTrackSelector(context, videoTrackSelectionFactory)
        player = ExoPlayer.Builder(context).setTrackSelector(trackSelector).build()
        setupPlayerListener()
    }

    fun sendTimeAndSelectedTracksUpdate() {
        val positionMillis = player.currentPosition
        val positionSec = TimeUnit.MILLISECONDS.toSeconds(positionMillis)

        val tracks = player.currentTracks
        val selectedTracks = mutableListOf<String>()
        for (trackGroup in tracks.groups) {
            for (i in 0 until trackGroup.length) {
                val isSelected = trackGroup.isTrackSelected(i)
                val trackFormat = trackGroup.getTrackFormat(i)
                if (isSelected) {
                    selectedTracks.add("Mime type: ${trackFormat.containerMimeType} codecs: ${trackFormat.codecs} bitrate: ${trackFormat.bitrate}")
                }
            }
        }

        onPlaybackTimeAndSelectedTracksUpdate(positionSec, selectedTracks)
    }

    private fun schedule() {
        Handler(Looper.getMainLooper()).postDelayed(
            runnable, 1000
        )
    }

    private fun setupPlayerListener() {
        player.addAnalyticsListener(object : AnalyticsListener {

            override fun onPlaybackStateChanged(
                eventTime: AnalyticsListener.EventTime,
                playbackState: Int
            ) {
                when (playbackState) {
                    ExoPlayer.STATE_BUFFERING -> {}
                    ExoPlayer.STATE_IDLE -> {}
                    ExoPlayer.STATE_READY -> {
                        if (player.isPlaying) {
                            onPlaybackStarted.invoke()
                            schedule()
                        }
                    }
                    ExoPlayer.STATE_ENDED -> {}
                }
            }

            override fun onLoadCompleted(
                eventTime: AnalyticsListener.EventTime,
                loadEventInfo: LoadEventInfo,
                mediaLoadData: MediaLoadData
            ) {

                mediaLoadData.trackFormat?.let { format ->
                    if (previousBitrate != format.bitrate) {
                        onBitrateUpdate(previousBitrate, format.bitrate)
                        previousBitrate = format.bitrate
                    }
                }
            }
        })
    }

    fun createAndPlayVideo(url: String): ExoPlayer {
        val mediaSource = createMediaSource(url)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
        return player
    }

    private fun createMediaSource(url: String): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))
    }
}
