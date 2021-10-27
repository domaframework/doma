package org.seasar.doma.it.dao;

import java.sql.SQLXML;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.Product;

@Dao
public interface ProductDao {

  @SQLXMLFactory
  SQLXML createSQLXML();

  @Insert
  int insert(Product product);

  @Select
  Product selectById(Integer id);
}
