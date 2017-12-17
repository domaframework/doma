package org.seasar.doma.jdbc.builder;

class ParamIndex {

    private int value = 1;

    void increment() {
        value++;
    }

    int getValue() {
        return value;
    }

}
