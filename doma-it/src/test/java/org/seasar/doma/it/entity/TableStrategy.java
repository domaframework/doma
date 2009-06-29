package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.ValueDomain;

@Entity
@Table(name = "TABLE_STRATEGY")
public interface TableStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @TableGenerator(pkColumnValue = "TABLE_STRATEGY_ID", allocationSize = 50)
    IdDomain id();

    ValueDomain value();
}
