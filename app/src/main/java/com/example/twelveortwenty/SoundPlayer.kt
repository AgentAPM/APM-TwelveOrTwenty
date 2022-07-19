package com.example.twelveortwenty

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build

object SoundPlayer {
    private lateinit var soundPool:SoundPool
    var sfxPlace:Int = 0
    var sfxRoll:Int = 0
    var sfxCombo4:Int = 0
    var sfxCombo5:Int = 0
    var sfxCombo8:Int = 0
    var sfxCombo12:Int = 0
    var sfxCombo16:Int = 0
    fun fromContext(context:Context){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build()
                )
                .build()
        } else {
            soundPool = SoundPool(1, Int.SIZE_BYTES,10)
        }

        sfxPlace = soundPool.load(context, R.raw.sfx_place,1)
        sfxRoll = soundPool.load(context, R.raw.sfx_roll,5)
        sfxCombo4 = soundPool.load(context, R.raw.sfx_combo_4,10)
        sfxCombo5 = soundPool.load(context, R.raw.sfx_combo_5,11)
        sfxCombo8 = soundPool.load(context, R.raw.sfx_combo_8,14)
        sfxCombo12 = soundPool.load(context, R.raw.sfx_combo_12,18)
        sfxCombo16 = soundPool.load(context, R.raw.sfx_combo_16,22)
    }
    fun play(soundID:Int){
        when(soundID){
            sfxPlace -> soundPool.play(soundID, 1.0F, 1.0F,1,0,1.0F)
            sfxRoll -> soundPool.play(soundID, 1.0F, 1.0F,5,0,1.0F)
            sfxCombo4  -> soundPool.play(soundID, 1.0F, 1.0F,10,0,1.0F)
            sfxCombo5  -> soundPool.play(soundID, 1.0F, 1.0F,11,0,1.0F)
            sfxCombo8  -> soundPool.play(soundID, 1.0F, 1.0F,14,0,1.0F)
            sfxCombo12 -> soundPool.play(soundID, 1.0F, 1.0F,18,0,1.0F)
            sfxCombo16 -> soundPool.play(soundID, 1.0F, 1.0F,22,0,1.0F)
        }
    }
}