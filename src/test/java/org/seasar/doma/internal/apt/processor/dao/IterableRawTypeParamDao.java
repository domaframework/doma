package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IterableRawTypeParamDao {

    @SuppressWarnings("rawtypes")
    @Select
    int select(List<Height> heightList);
}
