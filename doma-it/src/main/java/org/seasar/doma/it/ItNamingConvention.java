package org.seasar.doma.it;

import org.seasar.doma.jdbc.entity.CamelNamingConvention;

public class ItNamingConvention extends CamelNamingConvention {

    @Override
    public String fromEntityToTable(String entityName) {
        return super.fromEntityToTable(entityName).toUpperCase();
    }

    @Override
    public String fromPropertyToColumn(String propertyName) {
        return super.fromPropertyToColumn(propertyName).toUpperCase();
    }

}
