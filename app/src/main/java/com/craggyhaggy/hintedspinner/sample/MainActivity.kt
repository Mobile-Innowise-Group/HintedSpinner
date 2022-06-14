package com.craggyhaggy.hintedspinner.sample

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.craggyhaggy.hintedspinner.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf("Derek", "Kyrre", "Edrik", "Myriamm", "Alamar", "Gunnar")

        binding.hintedSpinner.apply {
            setItems(
                items,
                R.layout.layout_simple_hinted_spinner_item,
                R.layout.layout_simple_drop_down_hinted_spinner_item,
                R.id.item_id
            )
            setOnSelectItemAction {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        with(binding) {
            hintedSpinner.apply {
                withDivider.setOnClickListener {
                    if (withDivider.isChecked)
                        setWithDivider(true)
                    else
                        setWithDivider(false)
                }
                dividerColorGreen.setOnClickListener {
                    setDividerColor(Color.GREEN)
                }
                dividerColorBlue.setOnClickListener {
                    setDividerColor(Color.BLUE)
                }
                dividerColorRed.setOnClickListener {
                    setDividerColor(Color.RED)
                }
                popupBackgroundBlue.setOnClickListener {
                    setPopupBackground(R.color.blue)
                }
                popupBackgroundGreen.setOnClickListener {
                    setPopupBackground(R.color.green)
                }
                popupBackgroundRed.setOnClickListener {
                    setPopupBackground(R.color.red)
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
                selection1.setOnClickListener {
                    setSelection(0)
                }
                selection2.setOnClickListener {
                    setSelection(1)
                }
                selection3.setOnClickListener {
                    setSelection(2)
                }
                hintStyle1.setOnClickListener {
                    setHintStyle(R.style.TextAppearance_HintTextFirst)
                }
                hintStyle2.setOnClickListener {
                    setHintStyle(R.style.TextAppearance_HintTextSecond)
                }
                hintStyle3.setOnClickListener {
                    setHintStyle(R.style.TextAppearance_HintTextThird)
                }
                arrowColor1.setOnClickListener {
                    setArrowColor(Color.RED)
                }
                arrowColor2.setOnClickListener {
                    setArrowColor(Color.GREEN)
                }
                arrowColor3.setOnClickListener {
                    setArrowColor(Color.BLUE)
                }
                defaultStyle.setOnClickListener {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}