package org.seasar.doma.it;

import javax.sql.DataSource;

import org.seasar.framework.container.SingletonS2Container;

import doma.jdbc.Dialect;
import doma.jdbc.DomaAbstractConfig;
import doma.jdbc.JdbcLogger;

public class ItConfig extends DomaAbstractConfig {

    protected final JdbcLogger logger = new ItLogger();

    @Override
    public DataSource dataSource() {
        return SingletonS2Container.getComponent(DataSource.class);
    }

    @Override
    public Dialect dialect() {
        return SingletonS2Container.getComponent(Dialect.class);
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return logger;
    }

}
