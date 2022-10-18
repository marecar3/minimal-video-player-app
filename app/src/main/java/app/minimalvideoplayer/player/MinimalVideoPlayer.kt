package app.minimalvideoplayer.player

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.C
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

class MinimalVideoPlayer(context: Context) {

    var player: ExoPlayer

    init {
        val videoTrackSelectionFactory: ExoTrackSelection.Factory = AdaptiveTrackSelection.Factory()
        val trackSelector: TrackSelector = DefaultTrackSelector(context, videoTrackSelectionFactory)
        player = ExoPlayer.Builder(context).setTrackSelector(trackSelector).build()
        setupPlayerListener()
    }

    private fun setupPlayerListener() {
        player.addAnalyticsListener(object : AnalyticsListener {

            override fun onPlaybackStateChanged(
                eventTime: AnalyticsListener.EventTime,
                playbackState: Int
            ) {
                Log.e("ivica", "Promenilo se ${player.isPlaying} ${playbackState}")
                when (playbackState) {
                    ExoPlayer.STATE_BUFFERING ->             //You can use progress dialog to show user that video is preparing or buffering so please wait
                        Log.e("ivica", "buffering")
                    ExoPlayer.STATE_IDLE -> {
                        Log.e("ivica", "idle")
                    }
                    ExoPlayer.STATE_READY ->             // dismiss your dialog here because our video is ready to play now
                        Log.e("ivica", "ready")
                    ExoPlayer.STATE_ENDED -> {
                        Log.e("ivica", "ended")
                    }
                }
            }

            override fun onLoadCompleted(
                eventTime: AnalyticsListener.EventTime,
                loadEventInfo: LoadEventInfo,
                mediaLoadData: MediaLoadData
            ) {
                Log.e("ivica", "on load completed ${mediaLoadData.trackFormat?.bitrate}")
                Log.e("ivica", "${player.currentPosition}")
                val tracks = player.currentTracks

                for (trackGroup in tracks.groups) {
                    // Group level information.
                    val trackType: @C.TrackType Int = trackGroup.type
                    val trackInGroupIsSelected = trackGroup.isSelected
                    val trackInGroupIsSupported = trackGroup.isSupported

                    Log.e("ivica", "group ${trackGroup.isSelected} type: ${trackGroup.type}")

                    for (i in 0 until trackGroup.length) {
                        // Individual track information.
                        val isSupported = trackGroup.isTrackSupported(i)
                        val isSelected = trackGroup.isTrackSelected(i)
                        val trackFormat = trackGroup.getTrackFormat(i)

                        if (isSelected) {
                            Log.e("ivica", "format ${trackFormat} ${isSelected} ${player.currentMediaItemIndex}")
                        }
                    }
                }
            }
        })
    }

    fun setAndPlayVideo(url: String = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"): ExoPlayer {
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
