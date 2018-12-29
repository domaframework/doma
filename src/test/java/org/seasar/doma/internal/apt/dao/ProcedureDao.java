package org.seasar.doma.internal.apt.dao;

import java.util.List;
import java.util.Map;
import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface ProcedureDao {

  @Procedure(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  void execute(@ResultSet List<Map<String, Object>> result, @In int id);
}
