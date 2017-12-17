package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface FunctionDao {

    @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    List<Map<String, Object>> execute(@In int id);

}
