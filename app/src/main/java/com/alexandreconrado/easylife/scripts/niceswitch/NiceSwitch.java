package com.alexandreconrado.easylife.scripts.niceswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.ColorInt;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.BOUNCE_ANIM_AMPLITUDE_IN;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.BOUNCE_ANIM_AMPLITUDE_OUT;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.BOUNCE_ANIM_FREQUENCY_IN;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.BOUNCE_ANIM_FREQUENCY_OUT;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.COLOR_ANIMATION_DURATION;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.KEY_CHECKED;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.ON_CLICK_RADIUS_OFFSET;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.STATE;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.SWITCHER_ANIMATION_DURATION;
import static com.alexandreconrado.easylife.scripts.niceswitch.Constants.TRANSLATE_ANIMATION_DURATION;

import com.alexandreconrado.easylife.R;

@Keep
public class NiceSwitch extends View {
    private float iconRadius = 0f;
    private float iconClipRadius = 0f;
    private float iconCollapsedWidth = 0f;
    private float defHeight = 0;
    private float defWidth = 0;
    private boolean checked = true;

    private OnCheckedChangedListener onCheckedChangedListener;

    @ColorInt
    private int onColor = 0;
    @ColorInt
    private int offColor = 0;
    @ColorInt
    private int iconColor = 0;


    private RectF switcherRect = new RectF(0f, 0f, 0f, 0f);
    private Paint switcherPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF iconRect = new RectF(0f, 0f, 0f, 0f);
    private RectF iconClipRect = new RectF(0f, 0f, 0f, 0f);
    private Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint iconClipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private AnimatorSet animatorSet = new AnimatorSet();


    private float onClickRadiusOffset = 0f;

    public void setOnClickRadiusOffset(float value) {
        onClickRadiusOffset = value;
        switcherRect.left = value;
        switcherRect.top = value;
        switcherRect.right = getWidth() - value;
        switcherRect.bottom = getHeight() - value;
        invalidate();
    }


    @ColorInt
    private int currentColor = 0;

    public void setCurrentColor(@ColorInt int currentColor) {
        this.currentColor = currentColor;
        switcherPaint.setColor(currentColor);
        iconClipPaint.setColor(currentColor);
    }


    private float switcherCornerRadius = 0f;
    private float switchElevation = 0f;
    private float iconHeight = 0f;

    private float iconTranslateX = 0f;

    // from rounded rect to circle and back
    private float iconProgress = 0f;

    public void setIconProgress(float iconProgress) {
        this.iconProgress = iconProgress;
        float iconOffset = lerp(0f, iconRadius - iconCollapsedWidth / 2, iconProgress);
        iconRect.left = getWidth() - switcherCornerRadius - iconCollapsedWidth / 2 - iconOffset;
        iconRect.right = getWidth() - switcherCornerRadius + iconCollapsedWidth / 2 + iconOffset;

        float clipOffset = lerp(0f, iconClipRadius, iconProgress);
        iconClipRect.set(
                iconRect.centerX() - clipOffset,
                iconRect.centerY() - clipOffset,
                iconRect.centerX() + clipOffset,
                iconRect.centerY() + clipOffset
        );
        postInvalidateOnAnimation();
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public NiceSwitch(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NiceSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NiceSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NiceSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Switcher,
                defStyleAttr, R.style.BaseSwitcher);

        switchElevation = typedArray.getDimension(R.styleable.Switcher_android_elevation, 0f);

        onColor = typedArray.getColor(R.styleable.Switcher_switcher_on_color, 0);
        offColor = typedArray.getColor(R.styleable.Switcher_switcher_off_color, 0);
        iconColor = typedArray.getColor(R.styleable.Switcher_switcher_icon_color, 0);

        checked = typedArray.getBoolean(R.styleable.Switcher_android_checked, true);

        if (!checked) {
            setIconProgress(1f);
        }

        if (checked) {
            setCurrentColor(onColor);
        } else {
            setCurrentColor(offColor);
        }

        iconPaint.setColor(iconColor);

        defHeight = typedArray.getDimensionPixelOffset(R.styleable.Switcher_switcher_height, 0);
        defWidth = typedArray.getDimensionPixelOffset(R.styleable.Switcher_switcher_width, 0);

        typedArray.recycle();

        setOnClickListener(v -> {
            animateSwitch();
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        switcherRect.right = getWidth();
        switcherRect.bottom = getHeight();

        switcherCornerRadius = getHeight() / 2f;

        iconRadius = switcherCornerRadius * 0.6f;
        iconClipRadius = iconRadius / 2.25f;
        iconCollapsedWidth = iconRadius - iconClipRadius;

        iconHeight = iconRadius * 2f;

        iconRect.set(
                getWidth() - switcherCornerRadius - iconCollapsedWidth / 2,
                (getHeight() - iconHeight) / 2f,
                getWidth() - switcherCornerRadius + iconCollapsedWidth / 2,
                getHeight() - (getHeight() - iconHeight) / 2f
        );

        if (!checked) {
            iconRect.left = getWidth() - switcherCornerRadius - iconCollapsedWidth / 2 - (iconRadius - iconCollapsedWidth / 2);
            iconRect.right = getWidth() - switcherCornerRadius + iconCollapsedWidth / 2 + (iconRadius - iconCollapsedWidth / 2);

            iconClipRect.set(
                    iconRect.centerX() - iconClipRadius,
                    iconRect.centerY() - iconClipRadius,
                    iconRect.centerX() + iconClipRadius,
                    iconRect.centerY() + iconClipRadius
            );

            iconTranslateX = -(getWidth() - switcherCornerRadius * 2);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new SwitchOutline(w, h));
            setElevation(switchElevation);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            width = (int) defWidth;
            height = (int) defHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        // switcher
        canvas.drawRoundRect(switcherRect, switcherCornerRadius, switcherCornerRadius, switcherPaint);

        // icon
        int count = canvas.getSaveCount();
        try {
            canvas.translate(iconTranslateX, 0);
            canvas.drawRoundRect(iconRect, switcherCornerRadius, switcherCornerRadius, iconPaint);
            // don't draw clip path if icon is collapsed state (to prevent drawing small circle
            // on rounded rect when switch is checked)
            if (iconClipRect.width() > iconCollapsedWidth)
                canvas.drawRoundRect(iconClipRect, iconRadius, iconRadius, iconClipPaint);
        } finally {
            canvas.restoreToCount(count);
        }
    }


    private void animateSwitch() {
        animatorSet.cancel();
        animatorSet = new AnimatorSet();

        setOnClickRadiusOffset(ON_CLICK_RADIUS_OFFSET);

        float amplitude = BOUNCE_ANIM_AMPLITUDE_IN;
        float frequency = BOUNCE_ANIM_FREQUENCY_IN;
        float iconTranslateA = 0f;
        float iconTranslateB = -(getWidth() - switcherCornerRadius * 2);
        float newProgress = 1f;

        if (!checked) {
            amplitude = BOUNCE_ANIM_AMPLITUDE_OUT;
            frequency = BOUNCE_ANIM_FREQUENCY_OUT;
            iconTranslateA = iconTranslateB;
            iconTranslateB = 0f;
            newProgress = 0f;
        }

        ValueAnimator switcherAnimator = ValueAnimator.ofFloat(iconProgress, newProgress);
        switcherAnimator.addUpdateListener(animation -> {
            setIconProgress((float) animation.getAnimatedValue());
        });
        switcherAnimator.setInterpolator(new BounceInterpolator(amplitude, frequency));
        switcherAnimator.setDuration(SWITCHER_ANIMATION_DURATION);

        ValueAnimator translateAnimator = ValueAnimator.ofFloat(0f, 1f);
        float finalIconTranslateA = iconTranslateA;
        float finalIconTranslateB = iconTranslateB;
        translateAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            iconTranslateX = lerp(finalIconTranslateA, finalIconTranslateB, value);
        });
        translateAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickRadiusOffset(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translateAnimator.setDuration(TRANSLATE_ANIMATION_DURATION);


        int toColor;
        if (!checked) {
            toColor = onColor;
        } else {
            toColor = offColor;
        }

        iconClipPaint.setColor(toColor);

        ValueAnimator colorAnimator = new ValueAnimator();
        colorAnimator.addUpdateListener(animation -> {
            setCurrentColor((int) animation.getAnimatedValue());
        });
        colorAnimator.setIntValues(currentColor, toColor);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setDuration(COLOR_ANIMATION_DURATION);


        animatorSet.addListener(new AnimatorSet.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                checked = !checked;
                if (onCheckedChangedListener != null) {
                    onCheckedChangedListener.onCheckedChanged(checked);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.playTogether(switcherAnimator, translateAnimator, colorAnimator);
        animatorSet.start();
    }

    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            // this.checked = checked; // Remove this line
            forceCheck(); // Call forceCheck instead
            animateSwitch(); // Keep the animation if needed
        }
    }

    public boolean isChecked() {
        return checked;
    }

    private void forceCheck() {
        currentColor = offColor;
        iconProgress = 1f;
    }

    public OnCheckedChangedListener getOnCheckedChangedListener() {
        return onCheckedChangedListener;
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener onCheckedChangedListener) {
        this.onCheckedChangedListener = onCheckedChangedListener;
    }

    public float getOnClickRadiusOffset() {
        return onClickRadiusOffset;
    }

    public interface OnCheckedChangedListener {
        void onCheckedChanged(boolean checked);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CHECKED, checked);
        bundle.putParcelable(STATE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE));
            checked = ((Bundle) state).getBoolean(KEY_CHECKED);
            if (!checked) forceCheck();
        }
    }

    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new SwitchOutline(getWidth(), getHeight());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class SwitchOutline extends ViewOutlineProvider {
        private int width;
        private int height;

        public SwitchOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, width, height, switcherCornerRadius);
        }
    }
}
