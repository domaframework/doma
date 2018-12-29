package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

/** @author taedium */
@Entity
public class GeneratedValueNotNumberEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  String id;
}
