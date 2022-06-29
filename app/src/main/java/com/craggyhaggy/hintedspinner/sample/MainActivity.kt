package com.craggyhaggy.hintedspinner.sample

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.craggyhaggy.hintedspinner.sample.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.listeners.ColorListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Derek", "Kyrre", "Edrik", "Myriamm", "Alamar", "Gunnar")

        with(binding) {
            hintedSpinner.apply {
                setOnSelectItemAction {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                        .show()
                    binding.seekBarSize.isEnabled = false
                }
                seekBarSize.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seek: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val progressOfSize: Float = progress.toFloat()
                        setHintSize(progressOfSize)
                        hintSize.text = "${resources.getString(R.string.set_hint_size)} $progress"
                        hintSize.invalidate()
                    }

                    override fun onStartTrackingTouch(seek: SeekBar) {}
                    override fun onStopTrackingTouch(seek: SeekBar) {}
                })
                withDivider.setOnClickListener {
                    if (withDivider.isChecked)
                        setWithDivider(true)
                    else
                        setWithDivider(false)
                }

                dividerColorPicker.setColorListener(ColorListener { color, fromUser ->
                    if (fromUser)
                        setDividerColor(color)
                })

                arrowColorPicker.setColorListener(ColorListener { color, fromUser ->
                    if (fromUser)
                        setArrowColor(color)
                })
                hintColorPicker.setColorListener(ColorListener { color, fromUser ->
                    if (fromUser)
                        setHintColor(color)
                })
                popupBackgroundBlue.setOnClickListener {
                    setPopupBackground(R.color.blue)
                }
                popupBackgroundGreen.setOnClickListener {
                    setPopupBackground(R.color.green)
                }
                popupBackgroundRed.setOnClickListener {
                    setPopupBackground(R.color.red)
                }
                popupBackground1.setOnClickListener {
                    setPopupBackground(R.color.dark_green)
                }
                popupBackground2.setOnClickListener {
                    setPopupBackground(R.color.fuchsia)
                }
                hint1.setOnClickListener {
                    setHint(R.string.custom_hint_1)
                }
                hint2.setOnClickListener {
                    setHint(R.string.custom_hint_2)
                }
                hint3.setOnClickListener {
                    setHint(R.string.custom_hint_3)
                }
                arrowImage1.setOnClickListener {
                    setArrowImage(R.drawable.example_arrow_1)
                }
                arrowImage2.setOnClickListener {
                    setArrowImage(R.drawable.example_arrow_2)
                }
                arrowImage3.setOnClickListener {
                    setArrowImage(R.drawable.example_arrow_3)
                }
                arrowImage4.setOnClickListener {
                    setArrowImage(R.drawable.example_arrow_4)
                }
                arrowImage5.setOnClickListener {
                    setArrowImage(R.drawable.example_arrow_5)
                }
                hintNormal.setOnClickListener {
                    setHintAllCaps(false)
                }
                hintAllCaps.setOnClickListener {
                    setHintAllCaps(true)
                }
                defaultStyle.setOnClickListener {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}