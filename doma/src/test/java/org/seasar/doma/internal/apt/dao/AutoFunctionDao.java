package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.Out;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface AutoFunctionDao {

    @Function
    StringDomain executeFunction(@In IntegerDomain arg1, @Out IntegerDomain arg2);

}
