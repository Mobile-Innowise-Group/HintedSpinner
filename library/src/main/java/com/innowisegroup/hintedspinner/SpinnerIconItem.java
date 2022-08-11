package com.innowisegroup.hintedspinner;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class SpinnerIconItem {

    @DrawableRes
    private int imageId;

    @NonNull
    private String title;

    public SpinnerIconItem(@NonNull String title, @DrawableRes int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @DrawableRes
    public int getImageId() {
        return imageId;
    }

    public void setImageId(@DrawableRes int imageId) {
        this.imageId = imageId;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}