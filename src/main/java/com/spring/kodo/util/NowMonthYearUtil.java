package com.spring.kodo.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public class NowMonthYearUtil
{
    public static int getNowMonth()
    {
        LocalDate today = LocalDate.now();
        return today.getMonthValue(); // 12
    }

    public static Year getNowYear()
    {
        return Year.now(); // 2019
    }
}
