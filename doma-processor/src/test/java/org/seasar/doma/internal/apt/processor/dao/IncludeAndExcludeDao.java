package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.internal.apt.processor.entity.ImmutableUser;
import org.seasar.doma.jdbc.Result;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface IncludeAndExcludeDao {

  @Update(
      include = {"name", "salary"},
      exclude = {"salary"})
  int update(Emp emp);

  @Update(
      include = {"address.city"},
      exclude = {"address.street"})
  Result<ImmutableUser> update(ImmutableUser user);
}
