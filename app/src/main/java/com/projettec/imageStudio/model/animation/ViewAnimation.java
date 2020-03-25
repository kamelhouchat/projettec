package com.projettec.imageStudio.model.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * <p>
 * View Animation is a class that contains methods that allow you to apply
 * different animation to the views for different targets.
 * </p>
 *
 * @author Kamel.H
 * @see com.projettec.imageStudio.R.anim
 */

public class ViewAnimation {

    /**
     * <p>
     * Static method that replaces one view with another using an input animation and an output animation.
     * The method allows us to :
     *     <ul>
     *         <li>Define the duration before launch</li>
     *         <li>Define the duration of two animations</li>
     *     </ul>
     * </p>
     *
     * @param c            The activity context
     * @param inAnimation  The animation used to display the view
     * @param outAnimation The animation used to hide the view
     * @param toHide       The view we want to hide
     * @param toShow       The view we want to display
     * @param startOffset  The duration before the launch of the first animation
     * @param inDuration   The duration of the display animation
     * @param outDuration  The duration of the hide animation
     */
    public static void viewAnimatedChange(Context c, int inAnimation, int outAnimation, final View toHide,
                                          final View toShow, long startOffset, long inDuration, long outDuration) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, outAnimation);
        final Animation anim_in = AnimationUtils.loadAnimation(c, inAnimation);
        anim_in.setStartOffset(startOffset);
        anim_out.setDuration(outDuration);
        anim_in.setDuration(inDuration);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toHide.setVisibility(View.INVISIBLE);
                toShow.startAnimation(anim_in);
                toShow.setVisibility(View.VISIBLE);
            }
        });
        toHide.startAnimation(anim_out);
    }

    /**
     * <p>
     * Static method that allows you to replace the source image of an ImageView using the two animations:
     *     <ol>
     *         <li>Fade in</li>
     *         <li>Fade out</li>
     *     </ol>
     * </p>
     *
     * @param c         The activity context
     * @param v         ImageView on which the change is applied
     * @param new_image The new image we want to display
     */
    public static void imageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setDuration(200);
        anim_in.setDuration(200);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    /**
     * <p>
     * Static method that allows you to hide or display a view using animation
     * The method allows us to :
     *         <ul>
     *              <li>Define the duration before launch</li>
     *              <li>Define the duration the animations</li>
     *         </ul>
     * </p>
     *
     * @param c           The activity context
     * @param animation   The animation used to display or hide the view
     * @param toHide      The view we want to hide
     * @param startOffset The duration before the launch of the animation
     * @param duration    The duration of the animation
     * @param isShow      True, if we want to display the view and false if we want to hide it
     */
    public static void viewAnimatedHideOrShow(Context c, int animation, final View toHide, long startOffset, long duration, final boolean isShow) {
        final Animation anim = AnimationUtils.loadAnimation(c, animation);
        anim.setStartOffset(startOffset);
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShow) toHide.setVisibility(View.VISIBLE);
                else toHide.setVisibility(View.INVISIBLE);
            }
        });
        toHide.startAnimation(anim);
    }

}
