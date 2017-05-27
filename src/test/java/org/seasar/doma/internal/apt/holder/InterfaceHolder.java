package org.seasar.doma.internal.apt.holder;

import java.util.Arrays;

import org.seasar.doma.Holder;

@Holder(valueType = String.class, factoryMethod = "of")
public interface InterfaceHolder {

    String getValue();

    static InterfaceHolder of(String value) {
        return Arrays.<InterfaceHolder> stream(DefinedColor.values())
                .filter(a -> a.getValue().equals(value)).findFirst()
                .orElseGet(() -> new Color(value));
    }

    public enum DefinedColor implements InterfaceHolder {
        RED, BLUE, GREEN;

        @Override
        public String getValue() {
            return name();
        }
    }

    public static class Color implements InterfaceHolder {

        private final String value;

        public Color(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}
