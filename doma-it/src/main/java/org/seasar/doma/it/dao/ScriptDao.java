package org.seasar.doma.it.dao;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.Dao;
import org.seasar.doma.Script;
import org.seasar.doma.it.ItConfig;

@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface ScriptDao {

    static ScriptDao get() {
        return new ScriptDaoImpl();
    }

    @Script
    void create();

    @Script(haltOnError = false)
    void drop();
}
