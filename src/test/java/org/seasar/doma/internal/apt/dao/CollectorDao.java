package org.seasar.doma.internal.apt.dao;

import example.domain.PhoneNumber;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.Dao;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao(config = MyConfig.class)
public interface CollectorDao {

  @Select(strategy = SelectType.COLLECT)
  Integer selectByIdAndName(Integer id, String name, Collector<Emp, ?, Integer> collector);

  @Select(strategy = SelectType.COLLECT)
  <R> R selectById(Integer id, Collector<PhoneNumber, ?, R> collector);

  @Select(strategy = SelectType.COLLECT)
  <R extends Number> R select(Collector<String, ?, R> collector);

  @Select(strategy = SelectType.COLLECT)
  String selectWithHogeCollector(HogeCollector collector);

  @Select(strategy = SelectType.COLLECT, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  <R> R selectByIdAsMap(Integer id, Collector<Map<String, Object>, ?, R> collector);

  public class HogeCollector implements Collector<String, String, String> {

    @Override
    public Supplier<String> supplier() {
      return null;
    }

    @Override
    public BiConsumer<String, String> accumulator() {
      return null;
    }

    @Override
    public BinaryOperator<String> combiner() {
      return null;
    }

    @Override
    public Function<String, String> finisher() {
      return null;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return null;
    }
  }
}
