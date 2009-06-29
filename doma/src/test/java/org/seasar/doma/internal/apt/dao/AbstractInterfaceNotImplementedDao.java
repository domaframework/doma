package org.seasar.doma.internal.apt.dao;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;


/**
 * @author taedium
 * 
 */
public abstract class AbstractInterfaceNotImplementedDao extends
        DomaAbstractDao {

    public AbstractInterfaceNotImplementedDao(Config config,
            DataSource dataSource) {
        super(config, dataSource);
    }

}
