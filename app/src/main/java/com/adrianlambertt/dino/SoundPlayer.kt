package com.adrianlambertt.dino

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {
    fun playPlusDropSound(context: Context) {
        val b = MediaPlayer.create(context, R.raw.freesound_community_water_drop_plus_short)
        b.setOnCompletionListener {
            it.release()
        }

        b.start()
    }

    fun playMinusDropSound(context: Context) {
        val b = MediaPlayer.create(context, R.raw.freesound_community_water_drop_minus_short)
        b.setOnCompletionListener {
            it.release()
        }

        b.start()
    }
}