package org.seasar.doma.it.entity;

import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.ValueDomain;

import doma.Entity;
import doma.GeneratedValue;
import doma.GenerationType;
import doma.Id;
import doma.SequenceGenerator;
import doma.Table;

@Entity
@Table(name = "SEQUENCE_STRATEGY")
public interface SequenceStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "SEQUENCE_STRATEGY_ID", allocationSize = 50)
    IdDomain id();

    ValueDomain value();
}
