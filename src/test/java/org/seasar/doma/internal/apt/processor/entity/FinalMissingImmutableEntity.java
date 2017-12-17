package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

/**
 * @author taedium
 * 
 */
@Entity(immutable = true)
public class FinalMissingImmutableEntity {

    private final String aaa;

    private int bbb;

    private final Integer ccc;

    public FinalMissingImmutableEntity(String aaa, int bbb, Integer ccc) {
        this.aaa = aaa;
        this.bbb = bbb;
        this.ccc = ccc;
    }

    public String getAaa() {
        return aaa;
    }

    public int getBbb() {
        return bbb;
    }

    public Integer getCcc() {
        return ccc;
    }

}
