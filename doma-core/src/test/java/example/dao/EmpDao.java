package example.dao;

import example.entity.Emp;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface EmpDao {

  @Select
  Emp selectById(Integer id, SelectOptions option);

  @Select
  List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions option);

  @Select
  List<Emp> selectByExample(Emp emp);

  @Select(strategy = SelectType.STREAM)
  Integer stream(Function<Stream<Emp>, Integer> mapper);

  @Insert
  int insert(Emp entity);

  @Update
  int update(Emp entity);

  @Delete
  int delete(Emp entity);

  @Script
  void execute();
}
