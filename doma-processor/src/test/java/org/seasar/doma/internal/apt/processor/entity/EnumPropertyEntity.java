package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

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
