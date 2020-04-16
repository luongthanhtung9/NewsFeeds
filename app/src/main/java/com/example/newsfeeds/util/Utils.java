package com.example.newsfeeds.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_1)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_2)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_3)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_4)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_5)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_6)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_7)),
                    new ColorDrawable(Color.parseColor(Constant.COLOR_STRING_8))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static String DateToTimeFormat(String oldstringDate) {
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.PATTERN_DATE_TO_TIME,
                    Locale.ENGLISH);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String DateFormat(String oldstringDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.PATTERN_DATE, new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat(Constant.PATTERN_DATE_TO_TIME).parse(oldstringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldstringDate;
        }

        return newDate;
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        //String country = String.valueOf(locale.getCountry());
        //return country.toLowerCase();
        return "US";
    }
}
