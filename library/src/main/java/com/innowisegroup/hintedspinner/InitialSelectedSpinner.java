package com.innowisegroup.hintedspinner;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

class InitialSelectedSpinner extends AppCompatSpinner {
    public interface OnHintClickListener {
        void onHintClicked();
    }

    private static final String KEY_IS_INITIAL_SELECT = "KEY_IS_INITIAL_SELECT";
    private static final String KEY_SUPER_STATE = "KEY_SUPER_STATE";

    private boolean isSelected = false;
    private boolean isClicked = false;

    private OnHintClickListener onHintClickListener;

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

    public InitialSelectedSpinner(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int mode,
            OnHintClickListener onHintClickListener
    ) {
        super(context, attrs, defStyleAttr, mode);
        this.onHintClickListener = onHintClickListener;
    }

    @Override
    public boolean performClick() {
        isClicked = true;
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (isClicked) {
            onHintClickListener.onHintClicked();
        }
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        isSelected = true;
        getOnItemSelectedListener().onItemSelected(this, null, position, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (getAdapter() == null) {
            return;
        }

        final int selectedItemPosition = getSelectedItemPosition();

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