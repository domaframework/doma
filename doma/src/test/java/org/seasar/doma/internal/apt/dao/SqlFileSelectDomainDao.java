package org.seasar.doma.internal.apt.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.jdbc.SelectOptions;


/**
 * 
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface SqlFileSelectDomainDao {

    @Select
    StringDomain selectById(IntegerDomain id);

    @Select
    List<StringDomain> selectByNameAndSalary(StringDomain name,
            BigDecimalDomain salary, SelectOptions options);
}
