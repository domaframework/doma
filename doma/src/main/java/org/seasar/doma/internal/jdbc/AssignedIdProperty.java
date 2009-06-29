package org.seasar.doma.internal.jdbc;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class AssignedIdProperty<D extends Domain<?, ?>> extends
        BasicProperty<D> {

    public AssignedIdProperty(String name, String columnName, D domain,
            boolean insertable, boolean updatable) {
        super(name, columnName, domain, insertable, updatable);
    }

    @Override
    public boolean isId() {
        return true;
    }

}
