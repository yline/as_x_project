package com.android.launcher3;

import java.util.HashSet;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.launcher3.helper.FirstFrameAnimatorHelper;

/**
 * Animator的统一管理 + 帮助类
 */
public class LauncherAnimUtils
{
    private static HashSet<Animator> sAnimators = new HashSet<Animator>();
    
    private static Animator.AnimatorListener sEndAnimListener = new Animator.AnimatorListener()
    {
        public void onAnimationStart(Animator animation)
        {
            sAnimators.add(animation);
        }
        
        public void onAnimationRepeat(Animator animation)
        {
        }
        
        public void onAnimationEnd(Animator animation)
        {
            sAnimators.remove(animation);
        }
        
        public void onAnimationCancel(Animator animation)
        {
            sAnimators.remove(animation);
        }
    };
    
    public static void cancelOnDestroyActivity(Animator a)
    {
        a.addListener(sEndAnimListener);
    }
    
    public static void onDestroyActivity()
    {
        HashSet<Animator> animators = new HashSet<Animator>(sAnimators);
        for (Animator a : animators)
        {
            if (a.isRunning())
            {
                a.cancel();
            }
            else
            {
                sAnimators.remove(a);
            }
        }
    }
    
    // Helper method. Assumes a draw is pending, and that if the animation's duration is 0
    // it should be cancelled
    public static void startAnimationAfterNextDraw(final Animator animator, final View view)
    {
        view.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener()
        {
            private boolean mStarted = false;
            
            public void onDraw()
            {
                if (mStarted)
                {
                    return;
                }
                mStarted = true;
                // Use this as a signal that the animation was cancelled
                if (animator.getDuration() == 0)
                {
                    return;
                }
                animator.start();
                
                final ViewTreeObserver.OnDrawListener listener = this;
                view.post(new Runnable()
                {
                    public void run()
                    {
                        view.getViewTreeObserver().removeOnDrawListener(listener);
                    }
                });
            }
        });
    }
    
    public static AnimatorSet createAnimatorSet()
    {
        AnimatorSet anim = new AnimatorSet();
        cancelOnDestroyActivity(anim);
        return anim;
    }
    
    public static ValueAnimator ofFloat(View target, float... values)
    {
        ValueAnimator anim = new ValueAnimator();
        anim.setFloatValues(values);
        cancelOnDestroyActivity(anim);
        return anim;
    }
    
    public static ObjectAnimator ofFloat(View target, String propertyName, float... values)
    {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setPropertyName(propertyName);
        anim.setFloatValues(values);
        cancelOnDestroyActivity(anim);
        new FirstFrameAnimatorHelper(anim, target);
        return anim;
    }
    
    public static ObjectAnimator ofPropertyValuesHolder(View target, PropertyValuesHolder... values)
    {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        cancelOnDestroyActivity(anim);
        new FirstFrameAnimatorHelper(anim, target);
        return anim;
    }
    
    public static ObjectAnimator ofPropertyValuesHolder(Object target, View view, PropertyValuesHolder... values)
    {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        cancelOnDestroyActivity(anim);
        new FirstFrameAnimatorHelper(anim, view);
        return anim;
    }
}
