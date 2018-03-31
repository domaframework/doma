package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

@Entity
public class IllegalIdPropertyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "SEQ")
  IllegalId id;
}
