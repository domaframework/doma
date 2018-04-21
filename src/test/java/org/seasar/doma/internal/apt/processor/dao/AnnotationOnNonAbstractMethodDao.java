package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.jdbc.AbstractDao;
import org.seasar.doma.jdbc.Config;

@Dao(config = MyConfig.class)
public abstract class AnnotationOnNonAbstractMethodDao extends AbstractDao {

  public AnnotationOnNonAbstractMethodDao(Config config) {
    super(config);
  }

  @Insert
  public int insert() {
    return 0;
  }
}
