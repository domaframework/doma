package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import org.seasar.doma.*;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface ProcedureDao {

  @Procedure(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  void execute(@ResultSet List<Map<String, Object>> result, @In int id);
}
