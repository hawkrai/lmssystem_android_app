package com.celt.lms;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import static com.celt.lms.R.id.tabLayout;

public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
//    private int toolbarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.toolbarHeight = getToolbarHeight(context);
    }

//    private static int getToolbarHeight(Context context) {
//        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
//                new int[]{R.attr.actionBarSize});
//        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
//        styledAttributes.recycle();
//
//        return toolbarHeight;
//    }
//
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
//        return dependency instanceof AppBarLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
//        if (dependency instanceof AppBarLayout) {
//            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//            int fabBottomMargin = lp.bottomMargin;
//            int distanceToScroll = fab.getHeight() + fabBottomMargin;
//            float ratio;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                ratio = dependency.getY() / (float) toolbarHeight;
//                fab.setTranslationY(-distanceToScroll * ratio);
//            }
//        }
//        return true;
//    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (((TabLayout) coordinatorLayout.findViewById(tabLayout)).getSelectedTabPosition() == 0) {
            if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
            } else if (dyConsumed < 0 && child.getVisibility() == View.GONE) {
                child.show();
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}