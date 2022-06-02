package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

class InitialSelectedSpinner extends AppCompatSpinner {

    private static final String KEY_IS_INITIAL_SELECT = "KEY_IS_INITIAL_SELECT";
    private static final String KEY_SUPER_STATE = "KEY_SUPER_STATE";

    private boolean isSelected = false;

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

        isSelected = true;
        getOnItemSelectedListener().onItemSelected(this, null, position, 0);
    }

    void setInitialSelection(int position) {
        setSelection(position);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getAdapter() == null) {
            return;
        }

        final int selectedItemPosition = getSelectedItemPosition();

        //TODO (Исправить триггер метода setSelection дважды при выбранной позиции 0)
        if (selectedItemPosition == 0 && isSelected) {
            setSelection(selectedItemPosition);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(KEY_IS_INITIAL_SELECT, isSelected);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final Bundle bundle = (Bundle) state;

        final Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);
        super.onRestoreInstanceState(superState);

        isSelected = bundle.getBoolean(KEY_IS_INITIAL_SELECT);
    }
}