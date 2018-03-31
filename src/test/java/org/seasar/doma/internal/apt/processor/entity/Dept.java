package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/** @author taedium */
@Entity
public class Dept {

  @Id public PrimaryKey id;

  public Branch branch;
}
