package app.minimalvideoplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.minimalvideoplayer.databinding.ActivityMinimalVideoPlayerBinding
import app.minimalvideoplayer.player.MinimalVideoPlayer

class MinimalVideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMinimalVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMinimalVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val minimalVideoPlayer = MinimalVideoPlayer(applicationContext)
        binding.styledPlayerCv.player = minimalVideoPlayer.setAndPlayVideo()
    }
}