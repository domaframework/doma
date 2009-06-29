package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.ValueDomain;

@Entity
@Table(name = "IDENTITY_STRATEGY")
public interface IdentityStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    IdDomain id();

    ValueDomain value();
}
