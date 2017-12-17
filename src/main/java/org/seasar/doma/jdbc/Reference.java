package org.seasar.doma.jdbc;

/**
 * A reference to a value.
 * <p>
 * This class represents an OUT or INOUT parameter that is used in stored
 * procedures or stored functions.
 * 
 * @param <V>
 *            the value type
 */
public class Reference<V> {

    private V value;

    public Reference() {
    }

    public Reference(V value) {
        this.value = value;
    }

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

}
