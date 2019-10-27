package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.ViewCompat;

class InitialSelectedSpinner extends AppCompatSpinner {

    private boolean isInitialSelect = true;
    private int initialPosition = -1;

    public InitialSelectedSpinner(Context context) {
        this(context, null);
    }

    public InitialSelectedSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InitialSelectedSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        // Prevent double onItemSelected callback event,
        // when initial position is selected after another one.
        if (initialPosition != INVALID_POSITION && position != initialPosition && isInitialSelect) {
            isInitialSelect = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        // ConstraintLayout calls onLayout twice, so evaluate logic, after view is finally laid out.
        if (!ViewCompat.isLaidOut(this)) {
            return;
        }

        if (getAdapter() == null) {
            return;
        }

        final int selectedItemPosition = getSelectedItemPosition();
        if (initialPosition == INVALID_POSITION) {
            initialPosition = selectedItemPosition;
            return;
        }

        if (selectedItemPosition == initialPosition) {
            if (isInitialSelect) {
                isInitialSelect = false;
                if (getOnItemSelectedListener() != null) {
                    getOnItemSelectedListener().onItemSelected(this, null, selectedItemPosition, 0);
                }
            }
        }
    }
}
