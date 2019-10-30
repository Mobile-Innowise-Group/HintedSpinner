package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

class InitialSelectedSpinner extends AppCompatSpinner {

    private static final String KEY_INITIAL_POSITION = "KEY_INITIAL_POSITION";
    private static final String KEY_IS_INITIAL_SELECT = "KEY_IS_INITIAL_SELECT";
    private static final String KEY_SUPER_STATE = "KEY_SUPER_STATE";

    private boolean isInitialSelect = true;
    private boolean shouldForceSelection = false;
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

        if (position == initialPosition) {
            if (isInitialSelect) {
                isInitialSelect = false;
                if (getOnItemSelectedListener() != null) {
                    getOnItemSelectedListener().onItemSelected(this, null, position, 0);
                }
            }
        }
    }

    void setInitialSelection(int position) {
        // Right now, this logic triggers onItemSelected twice and outside callback is triggered.
        // Is it bug or feature?
        initialPosition = position;
        setSelection(position);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getAdapter() == null) {
            return;
        }

        final int selectedItemPosition = getSelectedItemPosition();
        if (initialPosition == INVALID_POSITION) {
            initialPosition = selectedItemPosition;
        }

        if (shouldForceSelection) {
            shouldForceSelection = false;
            isInitialSelect = true;
            setSelection(initialPosition);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_INITIAL_POSITION, getSelectedItemPosition());
        bundle.putBoolean(KEY_IS_INITIAL_SELECT, isInitialSelect);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);
        super.onRestoreInstanceState(superState);

        initialPosition = bundle.getInt(KEY_INITIAL_POSITION);
        isInitialSelect = bundle.getBoolean(KEY_IS_INITIAL_SELECT);
        // Spinner doesn't trigger onItemSelected method in case of selectedItemPosition = 0.
        // Force this logic, only when selectedItemPosition = 0 and nothing was selected.
        shouldForceSelection = initialPosition == 0 && !isInitialSelect;
    }
}
