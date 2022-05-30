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

    private boolean shouldForceSelection = true;
    private boolean isSelected = false;
    private int itemPosition = 0;

    public InitialSelectedSpinner(Context context) {
        super(context);
    }

    public InitialSelectedSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InitialSelectedSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InitialSelectedSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        if (getOnItemSelectedListener() != null) {
            isSelected = true;
            getOnItemSelectedListener().onItemSelected(this, null, position, 0);
        }
    }

    void setInitialSelection(int position) {
        itemPosition = position;
        setSelection(position);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getAdapter() == null) {
            return;
        }

        final int selectedItemPosition = getSelectedItemPosition();

        if (itemPosition == 0 && !isSelected) {
            //itemPosition == 0 && !isSelected -> hint field
            itemPosition = selectedItemPosition;
        }

        if (!shouldForceSelection) {
            shouldForceSelection = true;
            setSelection(itemPosition);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_INITIAL_POSITION, getSelectedItemPosition());
        bundle.putBoolean(KEY_IS_INITIAL_SELECT, isSelected);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);
        super.onRestoreInstanceState(superState);

        itemPosition = bundle.getInt(KEY_INITIAL_POSITION);
        isSelected = bundle.getBoolean(KEY_IS_INITIAL_SELECT);
        shouldForceSelection = (itemPosition == 0 && !isSelected);
    }
}