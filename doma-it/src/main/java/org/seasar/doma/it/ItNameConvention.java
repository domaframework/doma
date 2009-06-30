package org.seasar.doma.it;

import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.NameConvention;

public class ItNameConvention implements NameConvention {

    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        return entityName.toUpperCase();
    }

    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        return propertyName.toUpperCase();
    }

}
