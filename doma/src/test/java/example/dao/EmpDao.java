package example.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.Insert;
import org.seasar.doma.Iterate;
import org.seasar.doma.Procedure;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = ExampleConfig.class)
public interface EmpDao {

    @Select
    Emp selectById(IntegerDomain id, SelectOptions option);

    @Select
    List<Emp> selectByNameAndSalary(StringDomain name, BigDecimalDomain salary,
            SelectOptions option);

    @Iterate
    Integer iterate(IterationCallback<Integer, Emp> callback);

    @Insert
    int insert(Emp entity);

    @Update
    int update(Emp entity);

    @Delete
    int delete(Emp entity);

    @Function
    IntegerDomain add(@In IntegerDomain arg1, @In IntegerDomain arg2);

    @Function
    List<Emp> getEmps(@In IntegerDomain arg1, @In IntegerDomain arg2);

    @Procedure
    void exec(@In IntegerDomain arg1, @In IntegerDomain arg2);
}
