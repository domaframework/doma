package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.Dao;
import org.seasar.doma.jdbc.domain.ArrayDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface ArrayFactoryDao {

    @ArrayFactory(typeName = "varchar")
    ArrayDomain<String> update(String[] elements);
}
