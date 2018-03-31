package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

/** @author taedium */
@Entity
public class GeneratedValueWithCompositeIdEntity {

  @Id Integer id1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id2;
}
