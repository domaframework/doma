package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Script;

@SuppressWarnings("deprecation")
@Dao(config = MyConfig.class)
public interface ScriptDao {

  @Script
  void createTables();

  @Script(blockDelimiter = "GO", haltOnError = false)
  void dropTables();
}
