package com.CENAA.mydegreehelper;

import android.text.InputFilter;
import android.text.Spanned;

class GradeInputFilter implements InputFilter {

    private final double min;
    private final double max;

    public GradeInputFilter() {
        this.min = 0.0;
        this.max = 100.0;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException ignored) {}
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
