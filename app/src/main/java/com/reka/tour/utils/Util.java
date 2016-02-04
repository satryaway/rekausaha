package com.reka.tour.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by fachrifebrian on 2/4/16.
 */
public class Util {
    public static String toRupiahFormat(String nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatIDR = new DecimalFormatSymbols();

        formatIDR.setCurrencySymbol("IDR ");
        formatIDR.setMonetaryDecimalSeparator(',');
        formatIDR.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatIDR);
        kursIndonesia.format(Double.parseDouble(nominal));

        String result = kursIndonesia.format(Double.parseDouble(nominal)).substring(4);

        return "IDR " + result.substring(0, result.lastIndexOf(','));
    }
}
