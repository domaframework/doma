package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.internal.apt.javax.enterprise.context.ApplicationScoped;
import org.seasar.doma.internal.apt.processor.entity.Emp;

@Dao
@ApplicationScoped
public interface ApplicationScopedDao {
  @Insert
  int insert(Emp entity);
}
