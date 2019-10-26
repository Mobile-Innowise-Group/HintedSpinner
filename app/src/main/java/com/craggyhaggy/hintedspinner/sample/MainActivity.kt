package com.craggyhaggy.hintedspinner.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf("1", "2", "3", "4", "5", "6")
        )
        spinner.adapter = adapter
        spinner.setSelection(4)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@MainActivity, "$position", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
