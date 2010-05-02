package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Script;
import org.seasar.doma.it.ItConfig;

@Dao(config = ItConfig.class)
public interface ScriptDao {

    @Script
    void create();

    @Script(haltOnError = false)
    void drop();
}
