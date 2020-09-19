package org.seasar.doma.internal.apt.processor.dao.experimental;

import java.util.List;
import java.util.function.BiFunction;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;

@Dao
public interface SqlAnnotationDao {

  @Sql("select address from emp where name = /* name */'hoge'")
  @Select
  List<String> selectByName(String name);

  @Sql("select address from emp where name = /* name */'hoge'")
  @SqlProcessor
  List<String> selectByName(String name, BiFunction<Config, PreparedSql, List<String>> handler);

  @Sql("drop table address;")
  @Script
  void execute();

  @Sql("delete from emp where name = /* name */'hoge'")
  @Delete
  int delete(String name);

  @Sql("insert into emp (name) values(/* name */'hoge')")
  @Insert
  int insert(String name);

  @Sql("update emp set name = /* name */'hoge'")
  @Update
  int update(String name);

  @Sql("delete from emp where name = /* name */'hoge'")
  @BatchDelete
  int[] batchDelete(List<String> name);

  @Sql("insert into emp (name) values(/* name */'hoge')")
  @BatchInsert
  int[] batchInsert(List<String> name);

  @Sql("update emp set name = /* name */'hoge'")
  @BatchUpdate
  int[] batchUpdate(List<String> name);
}
