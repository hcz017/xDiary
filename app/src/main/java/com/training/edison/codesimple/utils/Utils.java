package com.training.edison.codesimple.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    public static final String BLOG_URL = "http://tenthorange.farbox.com/";
    // img
    public static final String IMG_URL_REG = "<img.*src=(.*?)[^>]*?>";
    // img src
    public static final String IMG_SRC_REG = "(http|https:)(.*?)(png|jpg|gif)(.*?)(?=\")";

    public static String formatDate(String time) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        SimpleDateFormat transToDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String localDate = null;
        try {
            //String转换成英文date，再转换成本机时间格式（String）
            localDate = dateFormat.format(transToDate.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDate;
    }
}
