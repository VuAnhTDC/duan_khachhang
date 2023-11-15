package com.example.duankhachhang.Class;

import java.text.DecimalFormat;
import java.util.Locale;

public class FormatMoneyVietNam {
    public static String formatMoneyVietNam(int money) {
        String formattedCurrency = "";
        Locale locale = new Locale("vi", "VN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.applyPattern("#,###.0");
        formattedCurrency = decimalFormat.format(money);
        return formattedCurrency;
    }
}
