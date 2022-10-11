package app.minimalvideoplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import app.minimalvideoplayer.databinding.ActivityMinimalVideoPlayerBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsManifest
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class MinimalVideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinimalVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinimalVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri("https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"))
        val player = ExoPlayer.Builder(this).build()
        player.setMediaSource(hlsMediaSource)
        player.prepare()
        binding.styledPlayerCv.player = player
        player.playWhenReady = true

        player.addListener(object: Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.e("Marko", "Changed $isPlaying")
            }

            override fun onTracksChanged(tracks: Tracks) {
                Log.e("Marko", "Changed ${tracks.groups.size}")
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                val manifest = player.currentManifest
                if (manifest != null) {
                    val hlsManifest = manifest as HlsManifest
                }
            }
        })
    }
}