package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.ParameterName;
import org.seasar.doma.Update;

/**
 * @author taedium
 * 
 */
@org.seasar.doma.GenericDao
public interface GenericDao<E> {

    @Insert
    int insert(@ParameterName("entity") E entity);

    @Update
    int update(@ParameterName("entity") E entity);

    @Delete
    int delete(@ParameterName("entity") E entity);
}
