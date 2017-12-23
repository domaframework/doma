package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface Issue214Dao {

    @Select
    Issue214Entity select(Issue214Entity entity);
}
