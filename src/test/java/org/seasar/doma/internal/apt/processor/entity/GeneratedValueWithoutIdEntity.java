package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;

/** @author taedium */
@Entity
public class GeneratedValueWithoutIdEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
}
