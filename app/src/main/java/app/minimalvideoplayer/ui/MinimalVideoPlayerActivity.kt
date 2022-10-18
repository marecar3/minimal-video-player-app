package app.minimalvideoplayer.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.minimalvideoplayer.BuildConfig
import app.minimalvideoplayer.databinding.ActivityMinimalVideoPlayerBinding
import app.minimalvideoplayer.player.MinimalVideoPlayer

class MinimalVideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinimalVideoPlayerBinding

    private val viewModel: MinimalVideoPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinimalVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val minimalVideoPlayer = MinimalVideoPlayer(
            applicationContext,
            onPlaybackStarted = {
                viewModel.trackPlaybackStarted()
            },
            onPlaybackTimeAndSelectedTracksUpdate = { positionSec, selectedTracks ->
                viewModel.trackPlaybackTimeAndSelectedTracks(positionSec, selectedTracks)
            },
            onBitrateUpdate = { previousBitrate, newBitrate ->
                viewModel.trackPlaybackBitrateUpdate(previousBitrate, newBitrate)
            })
        binding.styledPlayerCv.player = minimalVideoPlayer.createAndPlayVideo(BuildConfig.VIDEO_URL)
    }
}