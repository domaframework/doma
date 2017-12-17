package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

/**
 * @author nakamura-to
 *
 */
@Dao(config = MyConfig.class)
public interface OptionalMapListDao {

    @Select
    List<Optional<Map<String, Object>>> selectAll();

}
