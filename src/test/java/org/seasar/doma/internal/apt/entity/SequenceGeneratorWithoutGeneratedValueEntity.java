package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.SequenceGenerator;

@Entity
public class SequenceGeneratorWithoutGeneratedValueEntity {

  @SequenceGenerator(sequence = "SEQ")
  Integer id;
}
