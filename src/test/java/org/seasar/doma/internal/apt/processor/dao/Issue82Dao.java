package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;

import example.entity.Emp;

/**
 * @author nakamura-to
 *
 */
@Dao(config = MyConfig.class)
public interface Issue82Dao {

    @Update(sqlFile = true)
    int update(Emp entity, String name);
}
