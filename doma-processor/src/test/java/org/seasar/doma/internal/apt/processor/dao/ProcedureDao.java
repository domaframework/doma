package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;

@Dao
public interface ProcedureDao {

  @Procedure(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  void execute(@ResultSet List<Map<String, Object>> result, @In int id);
}
