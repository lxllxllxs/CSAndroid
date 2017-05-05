package com.yiyekeji.coolschool.bean;

import com.yiyekeji.coolschool.utils.DateUtils;

import java.util.Comparator;
import java.util.Date;

public class DateComParator implements Comparator<OtherOrder> {
    // TODO: 2017/5/3/003 最新 前面
        public int compare(OtherOrder c1, OtherOrder c2) {
            Date date1 = DateUtils.dateStringToDate(c1.getOrderTime());
            Date date2 = DateUtils.dateStringToDate(c2.getOrderTime());
            assert date2 != null;
            if (date2.before(date1)) {
                return -1;
            } else {
                return 1;
            }
        }
    }