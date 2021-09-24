package com.spring.kodo.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<String> getMonthListShortName()
    {
        List<String> monthList = new ArrayList<>();
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("Jul");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");

        return monthList;
    }
}
