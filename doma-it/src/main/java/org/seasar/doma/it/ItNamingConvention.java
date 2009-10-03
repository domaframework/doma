package org.seasar.doma.it;

import org.seasar.doma.jdbc.CamelNamingConvention;
import org.seasar.doma.jdbc.dialect.Dialect;

public class ItNamingConvention extends CamelNamingConvention {

    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        return super.fromEntityToTable(entityName, dialect).toUpperCase();
    }

    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        return super.fromPropertyToColumn(propertyName, dialect).toUpperCase();
    }

}
