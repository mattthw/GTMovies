package com.team19.gtmovies.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Austin Leal on 4/12/2016.
 * Animation to expand or shrink RecyclerView height.
 *
 * @author Austin Leal
 * @version 1.0
 */
public class AnimateRecyclerHeight extends Animation {
    private static AnimateRecyclerHeight upAnimate = null;
    private static AnimateRecyclerHeight downAnimate = null;
    private final View view;
    private final int targetHeight;
    private final boolean close;

    /**
     * Public constructor for AnimateRecyclerHeight.
     *
     * @param view view to animate
     * @param targetHeight height to set view to
     * @param close direction to animate
     */
    public AnimateRecyclerHeight(View view, int targetHeight,
            boolean close) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.close = close;
    }

    /**
     * Returns instance to use based on saving one down and one up instance.
     *
     * @param view view to animate
     * @param targetHeight
     * @param close
     * @return instance of
     */
    public static AnimateRecyclerHeight getInstance(View view, int targetHeight,
            boolean close) {
        if (close) {
            if (downAnimate == null
                    || downAnimate.view != view
                    || downAnimate.targetHeight != targetHeight) {
                downAnimate = new AnimateRecyclerHeight(view, targetHeight,
                        close);
            }
            return downAnimate;
        } else {
            if (upAnimate == null
                    || upAnimate.view != view
                    || upAnimate.targetHeight != targetHeight) {
                upAnimate = new AnimateRecyclerHeight(view, targetHeight, close);
            }
            return upAnimate;
        }
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (close) {
            newHeight = (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = (int) (targetHeight * (1 - interpolatedTime));
        }
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
