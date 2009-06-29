package org.seasar.doma.jdbc;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public class StandardNameConvention implements NameConvention {

    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        if (entityName == null) {
            throw new DomaIllegalArgumentException("entityName", entityName);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        return entityName;
    }

    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        if (propertyName == null) {
            throw new DomaIllegalArgumentException("propertyName", propertyName);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        return propertyName;
    }

}
