package com.innowisegroup.hintedspinner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;

import java.util.ArrayList;
import java.util.List;

public class HintedSpinner extends ConstraintLayout {
    public interface OnSelectItemAction {
        void onItemSelected(String item);
    }

    private InitialSelectedSpinner spinnerView;
    private TextView hintView;
    private ImageView arrowView;
    private View dividerView;

    private int cellGravity;
    private boolean isInitialSelect = true;
    private boolean isIconAnimated = false;
    private OnSelectItemAction onSelectItemAction;

    private static final int drawablePadding = 20;
    private static final int iconAnimationDuration = 300;
    private Float iconRotation = -180f;

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

    public void setItems(@NonNull List<String> items) {
        setItems(
                items,
                android.R.layout.simple_spinner_item,
                R.layout.support_simple_spinner_dropdown_item,
                android.R.id.text1
        );
    }

    public void setItemsWithIcons(@NonNull List<SpinnerIconItem> items) {
        setItemsWithIcons(
                items,
                R.layout.layout_spinner_item_with_icon,
                R.layout.support_simple_spinner_dropdown_item,
                R.id.title
        );
        invalidate();
    }

    private void setItemsWithIcons(
            @NonNull List<SpinnerIconItem> items,
            @LayoutRes int itemLayout,
            @LayoutRes int dropDownItemLayout,
            @IdRes int textViewId
    ) {
        adoptHintToItem(itemLayout, textViewId);

        final ArrayAdapter<SpinnerIconItem> adapter = new ArrayAdapter<SpinnerIconItem>(
                getContext(),
                itemLayout,
                textViewId,
                items
        ) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return getConvertView(getItem(position), convertView, parent, R.id.title);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return getConvertView(getItem(position), convertView, parent, R.id.title);
            }

            @NonNull
            private View getConvertView(
                    SpinnerIconItem item,
                    @Nullable View view,
                    @NonNull ViewGroup parent,
                    int resourceId
            ) {
                if (view == null) {
                    view = inflater.inflate(R.layout.layout_spinner_item_with_icon, parent, false);
                }

                TextView txtTitle = view.findViewById(resourceId);
                txtTitle.setText(item.getTitle());
                txtTitle.setCompoundDrawablesWithIntrinsicBounds(item.getImageId(), 0, 0, 0);
                txtTitle.setCompoundDrawablePadding(drawablePadding);
                txtTitle.setGravity(cellGravity);
                return view;
            }
        };

        adapter.setDropDownViewResource(dropDownItemLayout);
        spinnerView.setAdapter(adapter);
    }

    public void setItems(
            @NonNull List<String> items,
            @LayoutRes int itemLayout,
            @LayoutRes int dropDownItemLayout,
            @IdRes int textViewId
    ) {
        adoptHintToItem(itemLayout, textViewId);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), itemLayout, textViewId, items) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setGravity(cellGravity);
                setSizeOfSelectedSpinnerItem(view);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setGravity(cellGravity);
                return view;
            }
        };
        adapter.setDropDownViewResource(dropDownItemLayout);
        spinnerView.setAdapter(adapter);
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

    public void setHintTextSize(float size) {
        hintView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        if (spinnerView.getSelectedView() != null)
            ((TextView) spinnerView.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        invalidate();
    }

    public void setHintTextColor(@ColorInt int hintColor) {
        hintView.setTextColor(hintColor);
        invalidate();
    }

    public void setIconAnimation(boolean isIconAnimated) {
        this.isIconAnimated = isIconAnimated;
    }

    public void setCellGravity(int cellGravity) {
        this.cellGravity = cellGravity;
        hintView.setGravity(cellGravity);
        if (spinnerView.getSelectedView() != null) {
            ((TextView) spinnerView.getSelectedView()).setGravity(cellGravity);
        }
    }

    public void setPopupBackground(@ColorRes int color) {
        spinnerView.setPopupBackgroundResource(color);
        invalidate();
    }

    public void showDivider(boolean isDividerEnabled) {
        if (isDividerEnabled) {
            dividerView.setVisibility(VISIBLE);
        } else {
            dividerView.setVisibility(INVISIBLE);
        }
        invalidate();
    }

    public void setArrowDrawable(@DrawableRes int arrow) {
        arrowView.setImageResource(arrow);
        invalidate();
    }

    public void setDividerColor(@ColorInt int color) {
        dividerView.setBackgroundColor(color);
        invalidate();
    }

    public void setArrowTint(@ColorInt int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        ImageViewCompat.setImageTintList(arrowView, colorStateList);
        invalidate();
    }

    public void setOnSelectItemAction(OnSelectItemAction action) {
        onSelectItemAction = action;
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
        hintView.setOnClickListener(v -> {
                    setSizeOfSelectedSpinnerItem(spinnerView.getSelectedView());
                    spinnerView.performClick();
                }
        );
        arrowView.setOnClickListener(v -> {
                    setSizeOfSelectedSpinnerItem(spinnerView.getSelectedView());
                    spinnerView.performClick();
                }
        );
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isInitialSelect) {
                    isInitialSelect = false;
                } else {
                    hintView.setVisibility(GONE);
                    spinnerView.setVisibility(VISIBLE);
                    if (onSelectItemAction != null) {
                        onSelectItemAction.onItemSelected(spinnerView.getSelectedItem().toString());
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
            final boolean isDividerEnabled = array.getBoolean(
                    R.styleable.HintedSpinner_withDivider, false
            );
            @DrawableRes final int arrowRes = array.getResourceId(
                    R.styleable.HintedSpinner_arrowDrawable, R.drawable.ic_default_arrow
            );
            final String hint = array.getString(R.styleable.HintedSpinner_hint);
            @ColorInt final int dividerColor = array.getColor(
                    R.styleable.HintedSpinner_dividerColor, Color.GRAY
            );
            @ColorInt final int arrowTint = array.getColor(
                    R.styleable.HintedSpinner_arrowTint, Color.BLACK
            );
            final float hintTextSize = array.getDimension(
                    R.styleable.HintedSpinner_hintTextSize, getResources().getDimension(R.dimen.result_font)
            );
            final int hintTextColor = array.getColor(
                    R.styleable.HintedSpinner_hintTextColor, Color.GRAY
            );
            final int popupMode = array.getInteger(
                    R.styleable.HintedSpinner_popupMode, Spinner.MODE_DROPDOWN
            );
            @ColorRes final int popupBackground = array.getResourceId(
                    R.styleable.HintedSpinner_popupBackground,
                    android.R.color.darker_gray
            );
            final CharSequence[] items = array.getTextArray(
                    R.styleable.HintedSpinner_items
            );
            final int cellGravity = array.getInteger(
                    R.styleable.HintedSpinner_cellGravity,
                    Gravity.START
            );
            final boolean iconAnimation = array.getBoolean(
                    R.styleable.HintedSpinner_iconAnimation, false
            );

            initSpinner(context, attrs, defStyleAttr, popupMode);
            if (items != null) {
                List<String> itemsList = convertCharSequenceToList(items);
                setItems(itemsList);
            }
            setIconAnimation(iconAnimation);
            setHint(hint);
            setCellGravity(cellGravity);
            setHintTextColor(hintTextColor);
            hintView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSize);
            setArrowDrawable(arrowRes);
            setArrowTint(arrowTint);
            setDividerColor(dividerColor);
            showDivider(isDividerEnabled);
            setPopupBackground(popupBackground);
        } finally {
            array.recycle();
        }
    }

    private List<String> convertCharSequenceToList(CharSequence[] items) {
        List<String> list = new ArrayList<String>();
        for (CharSequence item : items) {
            list.add(item.toString());
        }
        return list;
    }

    private void onHintClicked() {
        if (isIconAnimated) {
            arrowView
                    .animate()
                    .setDuration(iconAnimationDuration)
                    .rotationBy(iconRotation = -iconRotation)
                    .start();
        }
    }

    private void setSizeOfSelectedSpinnerItem(View view) {
        if (view != null) {
            int hintHeight = hintView.getHeight();
            view.setMinimumHeight(hintHeight);
            view.setPadding(
                    hintView.getPaddingLeft(),
                    hintView.getPaddingTop(),
                    hintView.getPaddingRight(),
                    hintView.getPaddingBottom()
            );
            float hintTextSize = hintView.getTextSize();
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSize);
        }
    }

    private void initSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        final int spinnerId;
        spinnerView = new InitialSelectedSpinner(context, attrs, defStyleAttr, mode, this::onHintClicked);
        if (spinnerView.getId() == View.NO_ID) {
            spinnerId = ViewCompat.generateViewId();
            spinnerView.setId(spinnerId);
        } else {
            spinnerId = spinnerView.getId();
        }
        spinnerView.setVisibility(INVISIBLE);
        final LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        addView(spinnerView, lp);
        ConstraintSet set = new ConstraintSet();
        set.clone(this);
        set.connect(spinnerId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        set.connect(spinnerId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(spinnerId, ConstraintSet.END, R.id.arrow, ConstraintSet.START);
        set.connect(spinnerId, ConstraintSet.BOTTOM, R.id.divider, ConstraintSet.TOP);
        set.applyTo(this);
    }

    private void adoptHintToItem(@LayoutRes int itemLayout, @IdRes int textViewId) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(itemLayout, spinnerView, false);

        final TextView text;
        try {
            if (textViewId == 0) {
                text = (TextView) view;
            } else {
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