package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.entity.Emp;

@Dao
@AnnotationConfig
public interface AnnotationConfigDao {

  @Insert
  int insert(Emp emp);
}
