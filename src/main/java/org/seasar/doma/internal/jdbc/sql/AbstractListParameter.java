package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.jdbc.ListParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;

public abstract class AbstractListParameter<ELEMENT> implements ListParameter<ELEMENT> {

  protected final List<ELEMENT> list;

  protected final String name;

  public AbstractListParameter(List<ELEMENT> list, String name) {
    assertNotNull(list, name);
    this.list = list;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(ELEMENT element) {
    list.add(element);
  }

  @Override
  public Object getValue() {
    return list;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitListParameter(this, p);
  }
}
