package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel(prefix = "Q"))
public class Dept {
  @Id public Integer id;
  public Name name;
}
