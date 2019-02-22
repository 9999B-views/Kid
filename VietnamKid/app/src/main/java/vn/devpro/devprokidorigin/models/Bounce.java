package vn.devpro.devprokidorigin.models;

import android.view.animation.Interpolator;

public class Bounce implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 1;

    public Bounce(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (-1 * Math.pow(Math.E, -input / mAmplitude) *
                Math.cos(mFrequency * input) + 1);
    }
}
