package com.adrianlambertt.dino

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    fun playDropSound(context: Context) {
        val b = MediaPlayer.create(context, R.raw.freesound_community_water_drop_short)
        b.setOnCompletionListener {
            it.release()
        }

        b.start()
    }
}