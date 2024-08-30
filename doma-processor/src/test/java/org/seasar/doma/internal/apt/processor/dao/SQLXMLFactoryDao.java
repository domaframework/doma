package org.seasar.doma.internal.apt.processor.dao;

import java.sql.SQLXML;
import org.seasar.doma.Dao;
import org.seasar.doma.SQLXMLFactory;

@Dao
public interface SQLXMLFactoryDao {

  @SQLXMLFactory
  SQLXML create();
}
