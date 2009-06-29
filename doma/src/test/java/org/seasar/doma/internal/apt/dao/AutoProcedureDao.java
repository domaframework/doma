package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.domain.IntegerDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface AutoProcedureDao {

    @Procedure
    void executeProcedure(@In IntegerDomain arg1, @Out IntegerDomain arg2);

}
