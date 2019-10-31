package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;

public class HintedSpinner extends ConstraintLayout {

    public interface OnSelectItemAction {
        void onItemSelected(String item);
    }

    private InitialSelectedSpinner spinnerView;
    private TextView hintView;
    private ImageView arrowView;
    private View dividerView;

    private boolean isInitialSelect = true;
    private OnSelectItemAction onSelectItemAction;

    public HintedSpinner(Context context) {
        this(context, null);
    }

    public HintedSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HintedSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View.inflate(context, R.layout.layout_hinted_spinner, this);
        spinnerView = findViewById(R.id.spinner);
        hintView = findViewById(R.id.hint);
        arrowView = findViewById(R.id.arrow);
        dividerView = findViewById(R.id.divider);

        if (attrs != null) {
            applyAttributes(context, attrs, defStyleAttr);
        }

        hintView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerView.performClick();
            }
        });
        arrowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerView.performClick();
            }
        });
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSelect) {
                    isInitialSelect = false;
                } else {
                    hintView.setVisibility(INVISIBLE);
                    spinnerView.setVisibility(VISIBLE);

                    if (onSelectItemAction != null) {
                        onSelectItemAction.onItemSelected((String) spinnerView.getSelectedItem());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void applyAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.HintedSpinner, defStyleAttr, 0
        );
        try {
            final boolean withDivider = array.getBoolean(
                    R.styleable.HintedSpinner_with_divider, false
            );
            final @DrawableRes int arrowRes = array.getResourceId(
                    R.styleable.HintedSpinner_arrow_drawable, R.drawable.ic_default_arrow
            );
            final String hint = array.getString(R.styleable.HintedSpinner_hint);
            final @ColorInt int dividerTint = array.getColor(
                    R.styleable.HintedSpinner_divider_tint, Color.BLACK
            );
            final ColorStateList arrowTint = array.getColorStateList(
                    R.styleable.HintedSpinner_arrow_tint
            );

            hintView.setText(hint);
            arrowView.setImageResource(arrowRes);
            if (arrowTint != null) {
                ImageViewCompat.setImageTintList(arrowView, arrowTint);
            }
            if (withDivider) {
                dividerView.setBackgroundColor(dividerTint);
                dividerView.setVisibility(VISIBLE);
            } else {
                dividerView.setVisibility(INVISIBLE);
            }
        } finally {
            array.recycle();
        }
    }

    public void setItems(List<String> items, @LayoutRes int itemLayout) {
        spinnerView.setAdapter(new ArrayAdapter<>(getContext(), itemLayout, items));
    }

    public void setSelection(int position) {
        final SpinnerAdapter adapter = spinnerView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("Set adapter before call setSelection.");
        }

        if (position > adapter.getCount() - 1 || position < 0) {
            String message = "Selection should be less, than %d and positive.";
            throw new IllegalArgumentException(String.format(message, adapter.getCount()));
        }

        spinnerView.setInitialSelection(position);
    }

    public void setHint(@StringRes int hintRes) {
        hintView.setText(hintRes);
    }

    public void setHint(CharSequence hint) {
        hintView.setText(hint);
    }

    public void setOnSelectItemAction(OnSelectItemAction action) {
        onSelectItemAction = action;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    private static class SavedState extends BaseSavedState {

        SparseArray childrenStates;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader classLoader) {
            super(in);

            childrenStates = in.readSparseArray(classLoader);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeSparseArray(childrenStates);
        }

        public static final ClassLoaderCreator<SavedState> CREATOR
                = new ClassLoaderCreator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
