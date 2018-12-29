package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public abstract class AbstractEntity {

  @Id Integer id;
}
