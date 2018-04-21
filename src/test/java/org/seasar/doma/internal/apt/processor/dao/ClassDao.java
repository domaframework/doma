package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

@Dao(config = MyConfig.class)
public abstract class ClassDao extends AbstractDao {

  protected ClassDao(Config config) {
    super(config);
  }

  @Sql("select 1")
  @Select
  public abstract int select_public_abstract();

  @Sql("select 1")
  @Select
  protected abstract int select_protected_abstract();

  @Sql("select 1")
  @Select
  abstract int select_abstract();

  public int select_notFinal() {
    return this.select_abstract();
  }

  public final int select_final() {
    return this.select_abstract();
  }

  private int private_method() {
    return 0;
  }

  public static int static_method() {
    return 0;
  }
}
