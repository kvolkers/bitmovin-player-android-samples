package com.bitmovin.player.samples.playback.basic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitmovin.analytics.api.AnalyticsConfig
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.analytics.AnalyticsPlayerConfig
import com.bitmovin.player.api.source.SourceConfig
import com.bitmovin.player.samples.playback.basic.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val TEST_MANIFEST = "<test-manifest>"

class MainActivity : AppCompatActivity() {

    private lateinit var player: Player
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePlayer()
    }

    override fun onStart() {
        super.onStart()
        binding.playerView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.playerView.onResume()
    }

    override fun onPause() {
        binding.playerView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.playerView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.playerView.onDestroy()
        super.onDestroy()
    }

    private fun initializePlayer() {
        player = Player(
            context = this,
        ).also {
            binding.playerView.player = it
        }

        val now = Date()
        val oneMinuteAgo = Date(now.time - 60000)
        val oneMinuteLater = Date(now.time + 60000)

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val formattedOneMinuteAgo = formatter.format(oneMinuteAgo)
        val formattedOneMinuteLater = formatter.format(oneMinuteLater)

        val url = "$TEST_MANIFEST?t=$formattedOneMinuteAgo-$formattedOneMinuteLater"

        player.load(SourceConfig.fromUrl(url))
        player.play()
    }
}
