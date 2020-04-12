package com.cmechem.flashbeepshake

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.RuntimeException
import java.util.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var torchOn = false
        val cm: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cmList = cm.cameraIdList
        val camerawithFlash = mutableListOf<String>()
        val r =Random()
        val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        val vibrator:Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        for (camera in cmList) {
            Log.e("Camera", camera + cm.getCameraCharacteristics(camera))
            val camChar: CameraCharacteristics = cm.getCameraCharacteristics(camera)
            if (camChar.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!) {
                camerawithFlash.add(camera)
                cm.setTorchMode(camera, false)
            }

        }

        flashlight.setOnClickListener {
            if (!torchOn) {
                for (camera in camerawithFlash) {
                    cm.setTorchMode(camera, true)
                }
                flashlight.text = "Turn Flashlight Off"
                torchOn = true
            } else {
                for (camera in camerawithFlash) {
                    cm.setTorchMode(camera, false)
                }
                flashlight.text = "Turn Flashlight On"
                torchOn = false
            }
        }
        toneButton.setOnClickListener {
            try {
                val i= r.nextInt(100)+1
                tone.startTone(i, 600) //play specific tone for 600ms
                Log.e("Tone:",""+i)
            }
            catch (t:RuntimeException){
                Log.e("Error",t.toString())
            }
            }
        vibrateButton.setOnClickListener {
            Log.e("Version:",""+Build.VERSION.SDK_INT)
            if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
            }
        }
        }


    }


}

