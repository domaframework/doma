package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;

/**
 * @author taedium
 * 
 */
@Entity
public class NoDefaultConstructorSequenceIdGeneratorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "aaa", implementer = NoDefaultConstructorSequenceIdGenerator.class)
    Integer id;
}
