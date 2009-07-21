package org.seasar.doma.it;

import org.seasar.doma.jdbc.NameConvention;
import org.seasar.doma.jdbc.dialect.Dialect;

public class ItNameConvention implements NameConvention {

    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        return entityName.toUpperCase();
    }

    @Override
    public String fromTableToEntity(String tableName, Dialect dialect) {
        return tableName;
    }

    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        return propertyName.toUpperCase();
    }

    @Override
    public String fromColumnToProperty(String columnName, Dialect dialect) {
        return columnName;
    }

}
