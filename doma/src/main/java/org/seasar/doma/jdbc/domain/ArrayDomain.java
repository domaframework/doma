package org.seasar.doma.jdbc.domain;

import java.sql.Array;


/**
 * @author taedium
 * 
 */
public class ArrayDomain<E> extends AbstractArrayDomain<E> {

    public ArrayDomain() {
        super();
    }

    public ArrayDomain(Array v) {
        super(v);
    }

}
