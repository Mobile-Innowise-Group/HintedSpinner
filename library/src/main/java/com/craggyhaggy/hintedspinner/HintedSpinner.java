package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.TextViewCompat;

import java.util.List;

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

        hintView = findViewById(R.id.hint);
        arrowView = findViewById(R.id.arrow);
        dividerView = findViewById(R.id.divider);

        if (attrs != null) {
            applyAttributes(context, attrs, defStyleAttr);
        } else {
            initSpinner(context, null, defStyleAttr, Spinner.MODE_DROPDOWN);
        }
        hintView.setOnClickListener(v -> spinnerView.performClick());
        arrowView.setOnClickListener(v -> spinnerView.performClick());
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
                    R.styleable.HintedSpinner_withDivider, false
            );

            //тоже надо ли указывать?
            final @DrawableRes int arrowRes = array.getResourceId(
                    R.styleable.HintedSpinner_arrowDrawable, R.drawable.ic_default_arrow
            );
            final String hint = array.getString(R.styleable.HintedSpinner_hint);

            final @ColorInt int dividerTint = array.getColor(
                    R.styleable.HintedSpinner_dividerTint, Color.GRAY
            );

            //а нужно ли вообще менять цвет
            //если дефолтный подстраивается под тему
            final @ColorInt int arrowTint = array.getColor(
                    R.styleable.HintedSpinner_arrowTint, Color.BLACK
            );
            final int hintTextAppearance = array.getResourceId(
                    R.styleable.HintedSpinner_hintTextAppearance, R.style.TextAppearance_AppCompat
            );
            final int popupMode = array.getInteger(
                    R.styleable.HintedSpinner_popupMode, Spinner.MODE_DROPDOWN
            );
            final @ColorRes int popupBackground = array.getResourceId(
                    R.styleable.HintedSpinner_popupBackground,
                    android.R.color.darker_gray
            );

            initSpinner(context, attrs, defStyleAttr, popupMode);
            hintView.setText(hint);
            if (hintTextAppearance != -1) {
                TextViewCompat.setTextAppearance(hintView, hintTextAppearance);
            }
            arrowView.setImageResource(arrowRes);
            ColorStateList colorOfArrow = ColorStateList.valueOf(arrowTint);
            ImageViewCompat.setImageTintList(arrowView, colorOfArrow);
            dividerView.setBackgroundColor(dividerTint);
            if (withDivider) {
                dividerView.setVisibility(VISIBLE);
            } else {
                dividerView.setVisibility(INVISIBLE);
            }
            setPopupBackground(popupBackground);
        } finally {
            array.recycle();
        }
    }

    private void initSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        spinnerView = new InitialSelectedSpinner(context, attrs, defStyleAttr, mode);
        spinnerView.setVisibility(INVISIBLE);

        final LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        );
        ConstraintSet set = new ConstraintSet();
        set.clone(this);
        set.connect(spinnerView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(spinnerView.getId(), ConstraintSet.END, R.id.arrow, ConstraintSet.START);
        set.connect(spinnerView.getId(), ConstraintSet.BOTTOM, R.id.divider, ConstraintSet.TOP);
        set.applyTo(this);
        addView(spinnerView, lp);
    }

    public void setItems(@NonNull List<String> items) {
        setItems(
                items,
                android.R.layout.simple_spinner_item,
                R.layout.support_simple_spinner_dropdown_item,
                android.R.id.text1
        );
    }

    public void setItems(
            @NonNull List<String> items,
            @LayoutRes int itemLayout,
            @LayoutRes int dropDownItemLayout,
            @IdRes int textViewId
    ) {
        adoptHintToItem(itemLayout, textViewId);
        final ArrayAdapter adapter = new ArrayAdapter<>(
                getContext(), itemLayout, textViewId, items
        );
        adapter.setDropDownViewResource(dropDownItemLayout);
        spinnerView.setAdapter(adapter);
    }

    private void adoptHintToItem(@LayoutRes int itemLayout, @IdRes int textViewId) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(itemLayout, spinnerView, false);

        final TextView text;
        try {
            if (textViewId == 0) {
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = view.findViewById(textViewId);

                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + getContext().getResources().getResourceName(textViewId)
                            + " in item layout");
                }
            }

        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "HinterSpinner requires the resource ID to be a TextView", e);
        }
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

        spinnerView.setSelection(position);
    }

    public void setHint(@StringRes int hintRes) {
        hintView.setText(hintRes);
    }

    public void setHint(CharSequence hint) {
        hintView.setText(hint);
    }

    public void setPopupBackground(@ColorRes int color) {
        spinnerView.setPopupBackgroundResource(color);
        invalidate();
    }

    public void setWithDivider(boolean withDivider) {
        if (withDivider) {
            dividerView.setVisibility(VISIBLE);
        } else {
            dividerView.setVisibility(INVISIBLE);
        }
        invalidate();
    }

    public void setArrowImage(@DrawableRes int arrow) {
        arrowView.setImageResource(arrow);
        invalidate();
    }

    public void setDividerColor(@ColorInt int color) {
        dividerView.setBackgroundColor(color);
        invalidate();
    }

    public void setArrowColor(@ColorInt int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        ImageViewCompat.setImageTintList(arrowView, colorStateList);
        invalidate();
    }

    public void setHintStyle(@StyleRes int style) {
        if (style != -1) {
            TextViewCompat.setTextAppearance(hintView, style);
            invalidate();
        }
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

        SparseArray<Parcelable> childrenStates;

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