package com.example.rgb_controller

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener{

    private lateinit var colorView: View
    private lateinit var redPicker: SeekBar
    private lateinit var greenPicker: SeekBar
    private lateinit var bluePicker: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorView = findViewById(R.id.viewColor)
        colorView.setBackgroundColor(Color.BLACK)

        redPicker = findViewById(R.id.redPicker)
        redPicker.max = 255
        greenPicker = findViewById(R.id.greenPicker)
        greenPicker.max = 255
        bluePicker = findViewById(R.id.bluePicker)
        bluePicker.max = 255

        redPicker.setOnSeekBarChangeListener(this)
        greenPicker.setOnSeekBarChangeListener(this)
        bluePicker.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val config = Bitmap.Config.ARGB_8888
        val bitmap = viewColor.drawToBitmap(config)

        val redValue = Color.red(bitmap.getPixel(0, 0))
        val blueValue = Color.blue(bitmap.getPixel(0, 0))
        val greenValue = Color.green(bitmap.getPixel(0, 0))


        if(seekBar?.id == R.id.redPicker){
            colorView.setBackgroundColor(Color.rgb(progress, greenValue, blueValue))
        }else if(seekBar?.id == R.id.greenPicker){
            colorView.setBackgroundColor(Color.rgb(redValue, progress, blueValue))
        }else if(seekBar?.id == R.id.bluePicker){
            colorView.setBackgroundColor(Color.rgb(redValue, greenValue, progress))
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}