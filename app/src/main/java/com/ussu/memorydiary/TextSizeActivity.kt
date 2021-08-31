package com.ussu.memorydiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.ussu.memorydiary.databinding.ActivityTextSizeBinding


open class TextSizeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TextSizeActivity"
    }

    private lateinit var viewBinding: ActivityTextSizeBinding
    private var currentTheme = R.style.Theme_App_Medium
    private lateinit var pref: DefaultPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = DefaultPreferenceManager(this)

        val textSize = pref.getTextSize()
        currentTheme = getAppTheme(textSize)
        setTheme(currentTheme)

        viewBinding = ActivityTextSizeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initView()
    }

    override fun onResume() {
        super.onResume()

        val textSize = pref.getTextSize()
        val settingTheme = getAppTheme(textSize)

        if (currentTheme != settingTheme) {
            recreate()
        }
    }

    private fun initView() {
        viewBinding.textsizeSeekbar.progress = pref.getTextSize()
        viewBinding.textsizeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    pref.setTextSize(seekBar.progress)
                    if (currentTheme != getAppTheme(seekBar.progress)) {
                        recreate()
                    }
                }

            }
        })

    }


    private fun getAppTheme(textSize: Int) =
        when (textSize) {
            0 -> R.style.Theme_App_Small
            1 -> R.style.Theme_App_Medium
            2 -> R.style.Theme_App_Large
            else -> R.style.Theme_App_Medium
        }
}