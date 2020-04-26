package org.matrix.androidsdk.call;

import java.io.Serializable;

public class VideoLayoutConfiguration implements Serializable {
    public static final int INVALID_VALUE = -1;
    public int mDisplayHeight;
    public int mDisplayWidth;
    public int mHeight;
    public boolean mIsPortrait;
    public int mWidth;
    public int mX;
    public int mY;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VideoLayoutConfiguration{mIsPortrait=");
        sb.append(this.mIsPortrait);
        sb.append(", X=");
        sb.append(this.mX);
        sb.append(", Y=");
        sb.append(this.mY);
        sb.append(", Width=");
        sb.append(this.mWidth);
        sb.append(", Height=");
        sb.append(this.mHeight);
        sb.append('}');
        return sb.toString();
    }

    public VideoLayoutConfiguration(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, -1, -1);
    }

    public VideoLayoutConfiguration(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mX = i;
        this.mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        this.mDisplayWidth = i5;
        this.mDisplayHeight = i6;
    }

    public VideoLayoutConfiguration() {
        this.mX = -1;
        this.mY = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mDisplayWidth = -1;
        this.mDisplayHeight = -1;
    }
}
