package org.seasar.doma.internal.apt.processor.dao;

import javax.sql.DataSource;

import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author nakamura-to
 *
 */
@SingletonConfig
public class SingletonConfigAnnotatedConfig implements Config {

    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public Dialect getDialect() {
        return null;
    }

    public static SingletonConfigAnnotatedConfig singleton() {
        return null;
    }
}
