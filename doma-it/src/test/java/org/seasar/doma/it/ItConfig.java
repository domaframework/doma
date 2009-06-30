package org.seasar.doma.it;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Dialect;
import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.framework.container.SingletonS2Container;

public class ItConfig extends DomaAbstractConfig {

    protected static final JdbcLogger logger = new ItLogger();

    protected static final S2RequiresNewController controller = new S2RequiresNewController();

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

    @Override
    public RequiresNewController requiresNewController() {
        return controller;
    }

}
