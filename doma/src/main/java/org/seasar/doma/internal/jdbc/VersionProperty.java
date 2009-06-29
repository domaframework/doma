package org.seasar.doma.internal.jdbc;

import org.seasar.doma.domain.NumberDomain;

/**
 * @author taedium
 * 
 */
public class VersionProperty<D extends NumberDomain<?, ?>> extends
        BasicProperty<D> {

    public VersionProperty(String name, String columnName, D domain,
            boolean insertable, boolean updatable) {
        super(name, columnName, domain, insertable, updatable);
    }

    @Override
    public boolean isVersion() {
        return true;
    }

    public void setIfNecessary(Number value) {
        if (domain.isNull() || domain.get().intValue() < 0) {
            domain.set(value);
        }
    }

    public void increment() {
        if (domain.isNotNull()) {
            int i = domain.get().intValue();
            domain.set(i + 1);
        }
    }

}
