package org.devio.rn.splashscreen;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;

import java.lang.ref.WeakReference;

/**
 * SplashScreen
 * 启动屏
 * from：http://www.devio.org
 * Author:CrazyCodeBoy
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class SplashScreen {
    private static Dialog mSplashDialog;
    private static AnimatorSet mAnimation;
    private static WeakReference<Activity> mActivity;

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final int themeResId) {
        if (activity == null) return;
        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    mSplashDialog = new Dialog(activity, themeResId);
                    mSplashDialog.setContentView(R.layout.launch_screen);
                    mSplashDialog.setCancelable(false);

                    mAnimation = (AnimatorSet) AnimatorInflater
                            .loadAnimator(activity, R.animator.launch_screen_animation);
                    mAnimation.setTarget(mSplashDialog.findViewById(R.id.launch_screen_image));

                    // Make animation repeatable
                    mAnimation.addListener(new AnimatorListenerAdapter() {

                        private boolean mCanceled;

                        @Override
                        public void onAnimationStart(Animator animation) {
                            mCanceled = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            mCanceled = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (!mCanceled) {
                                mAnimation.start();
                            }
                        }

                    });

                    if (!mSplashDialog.isShowing()) {
                        mSplashDialog.show();
                        mAnimation.start();
                    }
                }
            }
        });
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final boolean fullScreen) {
        int resourceId = fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme;

        show(activity, resourceId);
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity) {
        show(activity, false);
    }

    /**
     * 关闭启动屏
     */
    public static void hide(Activity activity) {
        if (activity == null) {
            if (mActivity == null) {
                return;
            }
            activity = mActivity.get();
        }

        if (activity == null) return;

        final Activity _activity = activity;

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null && mSplashDialog.isShowing()) {
                    boolean isDestroyed = false;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = _activity.isDestroyed();
                    }

                    if (!_activity.isFinishing() && !isDestroyed) {
                        mAnimation.cancel();
                        mSplashDialog.dismiss();
                    }
                    mSplashDialog = null;
                }
            }
        });
    }
}
