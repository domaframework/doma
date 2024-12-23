package org.seasar.doma.internal.apt.processor.dao;

import java.util.Map;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.message.Message;

@Dao
public interface ResultStreamDao {

  @Select
  Stream<Emp> selectByIdAndName(Integer id, String name);

  @Select
  Stream<PhoneNumber> selectById(Integer id);

  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<String> select();

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  @Suppress(messages = {Message.DOMA4274})
  Stream<Map<String, Object>> selectByIdAsMap(Integer id);
}
