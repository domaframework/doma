package org.seasar.doma.it.entity;

import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.ValueDomain;

import doma.Entity;
import doma.GeneratedValue;
import doma.GenerationType;
import doma.Id;
import doma.Table;

@Entity
@Table(name = "IDENTITY_STRATEGY")
public interface IdentityStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    IdDomain id();

    ValueDomain value();
}
