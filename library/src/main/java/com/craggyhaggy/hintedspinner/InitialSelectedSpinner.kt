package com.craggyhaggy.hintedspinner

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class InitialSelectedSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    private var isInitialSelect = true
    private var initialPosition = -1

    override fun setSelection(position: Int) {
        super.setSelection(position)

        // Prevent double onItemSelected callback event,
        // when initial position is selected after another one.
        if (initialPosition != INVALID_POSITION && position != initialPosition && isInitialSelect) {
            isInitialSelect = false
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (adapter == null) {
            return
        }

        if (initialPosition == INVALID_POSITION) {
            initialPosition = selectedItemPosition
            return
        }

        if (selectedItemPosition == initialPosition) {
            if (isInitialSelect) {
                isInitialSelect = false
                onItemSelectedListener?.onItemSelected(this, null, selectedItemPosition, 0)
            }
        }
    }
}
