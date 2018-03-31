package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

/** @author taedium */
@Entity
public class NoDefaultConstructorSequenceIdGeneratorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "aaa", implementer = NoDefaultConstructorSequenceIdGenerator.class)
  Integer id;
}
