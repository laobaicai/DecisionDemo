package com.test.demo.constants;

/**
 * Created by caicai on 2017/9/6.
 */

public enum HotelTipType {
    GO_OUT(1), CHECK_IN(2), LIVE_IN(3), CHECK_OUT(4);

    private int value;

    HotelTipType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
