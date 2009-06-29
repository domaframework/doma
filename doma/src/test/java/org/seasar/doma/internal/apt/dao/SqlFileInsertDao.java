package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface SqlFileInsertDao {

    @Insert(sqlFile = true)
    int insert(IntegerDomain id, BigDecimalDomain salary);
}
