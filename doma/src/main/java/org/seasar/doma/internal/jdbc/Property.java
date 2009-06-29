package org.seasar.doma.internal.jdbc;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;

public interface Property<D extends Domain<?, ?>> {

    D getDomain();

    public String getName();

    public String getColumnName(Config config);

    public boolean isId();

    public boolean isVersion();

    public boolean isInsertable();

    public boolean isUpdatable();

}
