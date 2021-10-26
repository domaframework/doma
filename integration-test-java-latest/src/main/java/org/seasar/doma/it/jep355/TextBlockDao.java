package org.seasar.doma.it.jep355;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;

@Dao
public interface TextBlockDao {

  @Sql("""
        select
          /*%expand*/*
        from
          EMPLOYEE
        """)
  @Select
  List<Employee> selectAll();

  @Sql(
      """
        select
          /*%expand*/*
        from
          EMPLOYEE
        where
          EMPLOYEE_ID = /*id*/0
        """)
  @Select
  Employee selectById(Integer id);
}
