package org.seasar.doma.it.dao;

import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.TableStrategy;

import doma.Dao;

@Dao(config = ItConfig.class)
public interface TableStrategyDao extends GenericDao<TableStrategy> {

}
