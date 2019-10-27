package com.craggyhaggy.hintedspinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    public void setItems(List<String> items, @LayoutRes int itemLayout, int selection) {
        if (selection > items.size() - 1 || selection < 0) {
            String message = "Selection should be less, than %d and positive.";
            throw new IllegalArgumentException(String.format(message, items.size()));
        }

        spinner.setAdapter(new ArrayAdapter<>(getContext(), itemLayout, items));
        spinner.setSelection(selection);
    }
}
