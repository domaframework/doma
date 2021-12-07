package org.seasar.doma.it.criteria;

import java.util.UUID;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel)
public class Book {
  @Id public UUID id;
  public String title;
}
