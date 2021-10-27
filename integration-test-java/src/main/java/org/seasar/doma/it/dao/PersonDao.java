package org.seasar.doma.it.dao;

import java.util.List;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.Person;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

@Dao
public interface PersonDao {

  @Select
  Person selectById(Integer employeeId);

  @Delete
  Result<Person> delete(Person entity);

  @BatchDelete
  BatchResult<Person> delete(List<Person> entity);

  @Delete(sqlFile = true)
  Result<Person> deleteBySqlFile(Person entity);

  @BatchDelete(sqlFile = true)
  BatchResult<Person> deleteBySqlFile(List<Person> entity);
}
