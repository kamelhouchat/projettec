package com.projettec.imageStudio.model.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ViewAnimation {

    public static void viewAnimatedChange(Context c, int inAnimation, int outAnimation, final View toHide,
                                          final View toShow, long startOffset, long inDuration, long outDuration) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, outAnimation);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, inAnimation);
        anim_in.setStartOffset(startOffset);
        anim_out.setDuration(outDuration);
        anim_in.setDuration(inDuration);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                toHide.setVisibility(View.INVISIBLE);
                toShow.startAnimation(anim_in);
                toShow.setVisibility(View.VISIBLE);
            }
        });
        toHide.startAnimation(anim_out);
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setDuration(200);
        anim_in.setDuration(200);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public static void viewAnimatedHideOrShow(Context c, int animation, final View toHide, long startOffset, long duration, final boolean isShow){
        final Animation anim = AnimationUtils.loadAnimation(c, animation);
        anim.setStartOffset(startOffset);
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                if (isShow) toHide.setVisibility(View.VISIBLE);
                else toHide.setVisibility(View.INVISIBLE);
            }
        });
        toHide.startAnimation(anim);
    }

}
