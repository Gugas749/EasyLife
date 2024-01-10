package com.example.easylife.scripts.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class ColorPickerSquare extends View {
    private Paint mPaint;
    private Shader mLuar;
    private final float[] color = new float[]{1f, 1f, 1f};

    public ColorPickerSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerSquare(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mLuar = new LinearGradient(0f, 0f, 0f, this.getMeasuredHeight(), -0x1, -0x1000000, TileMode.CLAMP);
        }
        int rgb = Color.HSVToColor(color);
        Shader dalam = new LinearGradient(0f, 0f, this.getMeasuredWidth(), 0f, -0x1, rgb, TileMode.CLAMP);
        Shader shader = new ComposeShader(mLuar, dalam, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(shader);
        canvas.drawRect(0f, 0f, this.getMeasuredWidth(), this.getMeasuredHeight(), mPaint);
    }

    public void setHue(float hue) {
        color[0] = hue;
        invalidate();
    }
}