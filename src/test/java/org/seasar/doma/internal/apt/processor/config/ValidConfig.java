package org.seasar.doma.internal.apt.processor.config;

import javax.sql.DataSource;

import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author nakamura-to
 *
 */
@SingletonConfig
public class ValidConfig implements Config {

    private ValidConfig() {
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public Dialect getDialect() {
        return null;
    }

    public static ValidConfig singleton() {
        return new ValidConfig();
    }
}
