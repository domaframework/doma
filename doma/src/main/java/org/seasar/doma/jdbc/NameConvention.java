package org.seasar.doma.jdbc;


/**
 * @author taedium
 * 
 */
public interface NameConvention {

    String fromEntityToTable(String entityName, Dialect dialect);

    String fromPropertyToColumn(String propertyName, Dialect dialect);

}
