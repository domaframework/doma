package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.NoId;

@Dao(config = ItConfig.class)
public interface NoIdDao extends GenericDao<NoId> {

}
