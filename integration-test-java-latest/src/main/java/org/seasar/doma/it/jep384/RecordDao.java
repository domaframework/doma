package org.seasar.doma.it.jep384;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;

@Dao
public interface RecordDao {
  @Sql("""
        select
          /*%expand*/*
         from
          DEPARTMENT
        """)
  @Select
  List<Department> selectAll();

  @Sql(
      """
        select
          /*%expand*/*
        from
          DEPARTMENT
        where
          DEPARTMENT_ID = /*id*/0
        """)
  @Select
  Department selectById(Integer id);
}
