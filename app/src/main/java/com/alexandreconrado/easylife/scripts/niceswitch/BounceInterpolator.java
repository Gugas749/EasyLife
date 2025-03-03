package com.alexandreconrado.easylife.scripts.niceswitch;

import android.view.animation.Interpolator;

import androidx.annotation.Keep;

@Keep
public class BounceInterpolator implements Interpolator {
    private double amplitude;
    private double frequency;

    public BounceInterpolator(double amplitude, double frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / amplitude) * Math.cos(frequency * time) + 1);
    }
}
