package com.innowisegroup.hintedspinner;

import androidx.annotation.NonNull;

public class SpinnerIconItem {

    private int imageId;
    private String title;

    public SpinnerIconItem(String Title, int ImageId) {
        this.title = Title;
        this.imageId = ImageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int ImageId) {
        this.imageId = ImageId;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}