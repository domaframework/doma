package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.jdbc.Reference;

@Dao(config = MyConfig.class)
public interface FunctionDao {

  @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> execute(@In int id);

  @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  Age execute(@In Age arg1, @Out Reference<Age> arg2, @InOut Reference<Age> arg3);

  @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  Optional<Age> execute(
      @In Optional<Age> arg1,
      @Out Reference<Optional<Age>> arg2,
      @InOut Reference<Optional<Age>> arg3);

  @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Age> execute(@ResultSet List<Age> arg1);

  @Function(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Optional<Age>> execute(@ResultSet List<Optional<Age>> arg1, @In Optional<Age> arg2);
}
