package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public abstract class AbstractEntity {

  @Id Integer id;
}
