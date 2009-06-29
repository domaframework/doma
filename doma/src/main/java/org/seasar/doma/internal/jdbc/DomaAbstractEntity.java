package org.seasar.doma.internal.jdbc;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.NameConvention;

/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractEntity<I> implements Entity<I> {

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    public DomaAbstractEntity(String catalogName, String schemaName,
            String tableName) {
        __catalogName = catalogName;
        __schemaName = schemaName;
        __tableName = tableName;
    }

    public String __getQualifiedTableName(Config config) {
        StringBuilder buf = new StringBuilder();
        if (__catalogName != null) {
            buf.append(__catalogName).append(".");
        }
        if (__schemaName != null) {
            buf.append(__schemaName).append(".");
        }
        if (__tableName != null) {
            buf.append(__tableName);
        } else {
            Dialect dialect = config.dialect();
            NameConvention nameConvention = config.nameConvention();
            String tableName = nameConvention
                    .fromEntityToTable(__getName(), dialect);
            buf.append(tableName);
        }
        return buf.toString();
    }

}
