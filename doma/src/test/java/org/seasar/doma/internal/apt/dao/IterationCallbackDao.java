package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.IterationCallback;


/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IterationCallbackDao {

    @Select(iteration = true)
    Integer iterate(IntegerDomain id, StringDomain name,
            IterationCallback<Integer, Emp> callback);

    @Select(iteration = true)
    String iterate(HogeIterationCallback callback);
}
