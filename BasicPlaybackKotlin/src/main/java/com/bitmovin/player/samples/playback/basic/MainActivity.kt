package com.bitmovin.player.samples.playback.basic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import com.bitmovin.player.api.Player
import com.bitmovin.player.api.analytics.AnalyticsPlayerConfig
import com.bitmovin.player.api.event.PlayerEvent
import com.bitmovin.player.api.event.on
import com.bitmovin.player.api.live.LowLatencyConfig
import com.bitmovin.player.api.source.SourceConfig
import com.bitmovin.player.samples.playback.basic.databinding.ActivityMainBinding

private const val Sintel = "https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd"

private const val LL_MANIFEST = "<ll_manifest>"

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
            analyticsConfig = AnalyticsPlayerConfig.Disabled,
        ).apply {
            config.liveConfig.lowLatencyConfig = LowLatencyConfig(
                targetLatency = 10.0,
            )
        }.also {
            binding.playerView.player = it
        }

        player.on<PlayerEvent.TimeChanged> {
            Log.d("Low Latency", "latency=${player.lowLatency.latency}s")
        }
        player.on<PlayerEvent.StallStarted> {
            Log.d("Low Latency", "=== stalled ===")
        }

        player.load(SourceConfig.fromUrl(LL_MANIFEST))
        player.play()

        binding.playerView.postDelayed(15_000) {
            Log.d("Low Latency", "=== setting target latency to 10s again ===")
            player.lowLatency.targetLatency = 10.0
        }
    }
}
