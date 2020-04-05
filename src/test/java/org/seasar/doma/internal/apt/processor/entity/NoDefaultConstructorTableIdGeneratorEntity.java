package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.TableGenerator;

@Entity
public class NoDefaultConstructorTableIdGeneratorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @TableGenerator(pkColumnValue = "aaa", implementer = NoDefaultConstructorTableIdGenerator.class)
  Integer id;
}
