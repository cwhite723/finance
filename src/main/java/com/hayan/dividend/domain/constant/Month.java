package com.hayan.dividend.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Month {
    JAN(1),
    FEB(2),
    MAR(3),
    APR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AUG(8),
    SEP(9),
    OCT(10),
    NOV(11),
    DEC(12);

    private final int monthNumber;

    public static int getMonthNumberFromName(String name) {
        try {
            return Month.valueOf(name.toUpperCase()).getMonthNumber();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 달(월)입니다.");
        }
    }
}
