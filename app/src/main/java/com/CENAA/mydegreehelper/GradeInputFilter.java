package com.CENAA.mydegreehelper;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GradeInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Pattern pattern = Pattern.compile("[0-9]{0,3}+((\\.[0-9]?)?)|(\\.)?"); // Regex check for 2 decimals
        Matcher matcher = pattern.matcher(dest); // Match input to regex

        // Check if input is in range (0-100)
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(input) && matcher.matches()) {
                return null;
            }
        } catch (NumberFormatException ignored) {}
        return "";
    }

    private boolean isInRange(double input) {
        return 100.0 > 0.0 ? input >= 0.0 && input <= 100.0 : input >= 100.0 && input <= 0.0; }
}
