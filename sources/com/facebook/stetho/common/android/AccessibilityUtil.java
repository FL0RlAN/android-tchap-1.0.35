package com.facebook.stetho.common.android;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Spinner;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.List;

public final class AccessibilityUtil {
    private AccessibilityUtil() {
    }

    public static boolean hasText(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean z = false;
        if (accessibilityNodeInfoCompat == null) {
            return false;
        }
        if (!TextUtils.isEmpty(accessibilityNodeInfoCompat.getText()) || !TextUtils.isEmpty(accessibilityNodeInfoCompat.getContentDescription())) {
            z = true;
        }
        return z;
    }

    public static boolean isSpeakingNode(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, View view) {
        boolean z = false;
        if (!(accessibilityNodeInfoCompat == null || view == null)) {
            if (!accessibilityNodeInfoCompat.isVisibleToUser()) {
                return false;
            }
            int importantForAccessibility = ViewCompat.getImportantForAccessibility(view);
            if (importantForAccessibility != 4 && ((importantForAccessibility != 2 || accessibilityNodeInfoCompat.getChildCount() > 0) && (accessibilityNodeInfoCompat.isCheckable() || hasText(accessibilityNodeInfoCompat) || hasNonActionableSpeakingDescendants(accessibilityNodeInfoCompat, view)))) {
                z = true;
            }
        }
        return z;
    }

    public static boolean hasNonActionableSpeakingDescendants(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, View view) {
        if (!(accessibilityNodeInfoCompat == null || view == null || !(view instanceof ViewGroup))) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt != null) {
                    AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain();
                    try {
                        ViewCompat.onInitializeAccessibilityNodeInfo(childAt, obtain);
                        if (!isAccessibilityFocusable(obtain, childAt)) {
                            if (isSpeakingNode(obtain, childAt)) {
                                obtain.recycle();
                                return true;
                            }
                        }
                    } finally {
                        obtain.recycle();
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAccessibilityFocusable(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, View view) {
        boolean z = false;
        if (!(accessibilityNodeInfoCompat == null || view == null)) {
            if (!accessibilityNodeInfoCompat.isVisibleToUser()) {
                return false;
            }
            if (isActionableForAccessibility(accessibilityNodeInfoCompat)) {
                return true;
            }
            if (isTopLevelScrollItem(accessibilityNodeInfoCompat, view) && isSpeakingNode(accessibilityNodeInfoCompat, view)) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isTopLevelScrollItem(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, View view) {
        boolean z = false;
        if (accessibilityNodeInfoCompat == null || view == null) {
            return false;
        }
        View view2 = (View) ViewCompat.getParentForAccessibility(view);
        if (view2 == null) {
            return false;
        }
        if (accessibilityNodeInfoCompat.isScrollable()) {
            return true;
        }
        List actionList = accessibilityNodeInfoCompat.getActionList();
        if (actionList.contains(Integer.valueOf(4096)) || actionList.contains(Integer.valueOf(8192))) {
            return true;
        }
        if (view2 instanceof Spinner) {
            return false;
        }
        if ((view2 instanceof AdapterView) || (view2 instanceof ScrollView) || (view2 instanceof HorizontalScrollView)) {
            z = true;
        }
        return z;
    }

    public static boolean isActionableForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean z = false;
        if (accessibilityNodeInfoCompat == null) {
            return false;
        }
        if (accessibilityNodeInfoCompat.isClickable() || accessibilityNodeInfoCompat.isLongClickable() || accessibilityNodeInfoCompat.isFocusable()) {
            return true;
        }
        List actionList = accessibilityNodeInfoCompat.getActionList();
        if (actionList.contains(Integer.valueOf(16)) || actionList.contains(Integer.valueOf(32)) || actionList.contains(Integer.valueOf(1))) {
            z = true;
        }
        return z;
    }

    public static boolean hasFocusableAncestor(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, View view) {
        if (accessibilityNodeInfoCompat == null || view == null) {
            return false;
        }
        ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(view);
        if (!(parentForAccessibility instanceof View)) {
            return false;
        }
        AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain();
        try {
            ViewCompat.onInitializeAccessibilityNodeInfo((View) parentForAccessibility, obtain);
            if (obtain == null) {
                return false;
            }
            if (isAccessibilityFocusable(obtain, (View) parentForAccessibility)) {
                obtain.recycle();
                return true;
            } else if (hasFocusableAncestor(obtain, (View) parentForAccessibility)) {
                obtain.recycle();
                return true;
            } else {
                obtain.recycle();
                return false;
            }
        } finally {
            obtain.recycle();
        }
    }
}
