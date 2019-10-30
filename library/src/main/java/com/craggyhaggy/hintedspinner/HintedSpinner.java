package com.craggyhaggy.hintedspinner;

import android.content.Context;
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

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HintedSpinner extends ConstraintLayout {

    public interface OnSelectItemAction {
        void onItemSelected(String item);
    }

    private InitialSelectedSpinner spinner;
    private TextView hint;
    private ImageView arrow;

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

        spinner = findViewById(R.id.spinner);
        hint = findViewById(R.id.hint);
        arrow = findViewById(R.id.arrow);

        hint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSelect) {
                    isInitialSelect = false;
                } else {
                    hint.setVisibility(INVISIBLE);
                    spinner.setVisibility(VISIBLE);

                    if (onSelectItemAction != null) {
                        onSelectItemAction.onItemSelected((String) spinner.getSelectedItem());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setOnSelectItemAction(OnSelectItemAction action) {
        onSelectItemAction = action;
    }

    public void setItems(List<String> items, @LayoutRes int itemLayout) {
        spinner.setAdapter(new ArrayAdapter<>(getContext(), itemLayout, items));
    }

    public void setSelection(int position) {
        final SpinnerAdapter adapter = spinner.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("Set adapter before call setSelection.");
        }

        if (position > adapter.getCount() - 1 || position < 0) {
            String message = "Selection should be less, than %d and positive.";
            throw new IllegalArgumentException(String.format(message, adapter.getCount()));
        }

        spinner.setInitialSelection(position);
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
