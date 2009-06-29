package org.seasar.doma.internal.jdbc;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.NameConvention;

/**
 * @author taedium
 * 
 */
public class BasicProperty<D extends Domain<?, ?>> implements Property<D> {

    protected final String name;

    protected final String columnName;

    protected final D domain;

    protected final boolean insertable;

    protected final boolean updatable;

    public BasicProperty(String name, String columnName, D domain,
            boolean insertable, boolean updatable) {
        this.name = name;
        this.columnName = columnName;
        this.domain = domain;
        this.insertable = insertable;
        this.updatable = updatable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getColumnName(Config config) {
        if (columnName != null) {
            return columnName;
        }
        Dialect dialect = config.dialect();
        NameConvention nameConvention = config.nameConvention();
        return nameConvention.fromPropertyToColumn(name, dialect);
    }

    @Override
    public D getDomain() {
        return domain;
    }

    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public boolean isVersion() {
        return false;
    }

    @Override
    public boolean isInsertable() {
        return insertable;
    }

    @Override
    public boolean isUpdatable() {
        return updatable;
    }

}
