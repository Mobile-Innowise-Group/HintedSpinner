package com.craggyhaggy.hintedspinner.sample

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = listOf("Derek", "Kyrre", "Edrik", "Myriamm", "Alamar", "Gunnar")

        hintedspinner1.apply {
            setItems(items)
            setOnSelectItemAction {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        hintedspinner2.apply {
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

        ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            simple_spinner.adapter = adapter
        }
    }
}
