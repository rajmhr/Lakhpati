package com.lakhpati.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityMethod {
    public static String getDateString(Date dateIn) {
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
        String date = spf.format(dateIn);
        return date;
    }
}
