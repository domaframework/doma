package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

/** @author taedium */
@Entity
public class AbstractSequenceIdGeneratorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "aaa", implementer = AbstractSequenceIdGenerator.class)
  Integer id;
}
