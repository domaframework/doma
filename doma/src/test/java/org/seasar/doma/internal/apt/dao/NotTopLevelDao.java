package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;

/**
 * @author taedium
 * 
 */
public interface NotTopLevelDao {

    @Dao(config = MyConfig.class)
    interface Hoge {
    }
}
