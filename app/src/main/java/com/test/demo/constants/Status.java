package com.test.demo.constants;

/**
 * Created by caicai on 2017/9/6.
 */

public enum Status {
    NORMAL(0), INVALID(1), USED(2), REFUND(3);

    private int value;

    private Status(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
