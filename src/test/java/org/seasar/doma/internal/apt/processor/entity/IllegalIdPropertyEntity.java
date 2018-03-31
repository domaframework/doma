package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

/** @author taedium */
@Entity
public class IllegalIdPropertyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "SEQ")
  IllegalId id;
}
