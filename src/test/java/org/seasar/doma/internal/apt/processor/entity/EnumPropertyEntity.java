package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

/** @author taedium */
@Entity
public class EnumPropertyEntity {

  @Id Hoge id;

  Hoge hoge;

  enum Hoge {
    AAA,
    BBB,
    CCC
  }
}
