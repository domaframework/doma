package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.TableStrategy;

@Dao(config = ItConfig.class)
public interface TableStrategyDao extends GenericDao<TableStrategy> {

}
