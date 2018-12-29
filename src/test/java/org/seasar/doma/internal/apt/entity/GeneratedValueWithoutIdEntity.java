package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;

@Entity
public class GeneratedValueWithoutIdEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
}
