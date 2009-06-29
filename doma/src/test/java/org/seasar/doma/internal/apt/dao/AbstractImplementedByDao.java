package org.seasar.doma.internal.apt.dao;

import javax.sql.DataSource;

import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;


/**
 * @author taedium
 * 
 */

public abstract class AbstractImplementedByDao extends DomaAbstractDao
        implements ImplementedByDao {

    public AbstractImplementedByDao(Config config, DataSource dataSource) {
        super(config, dataSource);
    }

    @Override
    public int bbb(Emp entity) {
        return 0;
    }

    @Override
    public int delete(Emp entity) {
        return 0;
    }

    @Override
    public int insert(Emp entity) {
        return 0;
    }

}
