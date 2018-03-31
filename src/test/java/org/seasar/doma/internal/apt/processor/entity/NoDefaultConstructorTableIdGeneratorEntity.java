package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

@Entity
public class NoDefaultConstructorTableIdGeneratorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @TableGenerator(pkColumnValue = "aaa", implementer = NoDefaultConstructorTableIdGenerator.class)
  Integer id;
}
