package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Dept {

  @Id public PrimaryKey id;

  public Branch branch;
}
