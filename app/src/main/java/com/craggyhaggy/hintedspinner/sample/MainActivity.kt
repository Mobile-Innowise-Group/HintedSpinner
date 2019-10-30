package com.craggyhaggy.hintedspinner.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        hintedspinner1.setItems(
            listOf("1", "2", "3", "4", "5", "6"),
            android.R.layout.simple_spinner_item
        )
        hintedspinner1.setSelection(5)
        hintedspinner1.setOnSelectItemAction {
            Toast.makeText(this, it, Toast.LENGTH_SHORT)
                .show()
        }

        hintedspinner2.setItems(
            listOf("a", "b", "c", "d", "e", "f"),
            android.R.layout.simple_spinner_item
        )
        hintedspinner2.setOnSelectItemAction {
            Toast.makeText(this, it, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
