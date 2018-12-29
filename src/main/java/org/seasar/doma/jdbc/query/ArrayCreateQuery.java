package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

/** @author taedium */
public class ArrayCreateQuery extends AbstractCreateQuery<Array> {

  protected String typeName;

  protected Object[] elements;

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(typeName, elements);
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public Object[] getElements() {
    return elements;
  }

  public void setElements(Object[] elements) {
    this.elements = elements;
  }

  @Override
  public Array create(Connection connection) throws SQLException {
    return connection.createArrayOf(typeName, elements);
  }
}
