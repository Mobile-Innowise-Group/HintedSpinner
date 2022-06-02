package com.craggyhaggy.hintedspinner.sample

import android.os.Bundle
import android.widget.ArrayAdapter
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

        binding.hintedspinner1.apply {
            setItems(items)
            setOnSelectItemAction {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.hintedspinner2.apply {
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
            binding.simpleSpinner.adapter = adapter
        }
    }
}
