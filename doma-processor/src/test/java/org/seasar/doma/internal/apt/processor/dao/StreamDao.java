package org.seasar.doma.internal.apt.processor.dao;

import example.domain.PhoneNumber;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao(config = MyConfig.class)
public interface StreamDao {

  @Select(strategy = SelectType.STREAM)
  Integer selectByIdAndName(Integer id, String name, Function<Stream<Emp>, Integer> mapper);

  @Select(strategy = SelectType.STREAM)
  <R> R selectById(Integer id, Function<Stream<PhoneNumber>, R> mapper);

  @Select(strategy = SelectType.STREAM)
  <R extends Number> R select(Function<Stream<String>, R> mapper);

  @Select(strategy = SelectType.STREAM)
  String selectWithHogeFunction(HogeFunction mapper);

  @Select(strategy = SelectType.STREAM, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  <R> R selectByIdAsMap(Integer id, Function<Stream<Map<String, Object>>, R> callback);

  class HogeFunction implements Function<Stream<String>, String> {

    @Override
    public String apply(Stream<String> t) {
      return null;
    }
  }
}
