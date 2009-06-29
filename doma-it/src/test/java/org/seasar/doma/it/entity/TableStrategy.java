package org.seasar.doma.it.entity;

import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.ValueDomain;

import doma.Entity;
import doma.GeneratedValue;
import doma.GenerationType;
import doma.Id;
import doma.Table;
import doma.TableGenerator;

@Entity
@Table(name = "TABLE_STRATEGY")
public interface TableStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @TableGenerator(pkColumnValue = "TABLE_STRATEGY_ID", allocationSize = 50)
    IdDomain id();

    ValueDomain value();
}
