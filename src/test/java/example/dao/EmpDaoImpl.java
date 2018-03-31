package example.dao;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.command.*;
import org.seasar.doma.jdbc.query.*;

@Generated("")
public class EmpDaoImpl extends AbstractDao implements EmpDao {

  private static Method method0 =
      getDeclaredMethod(EmpDaoImpl.class, "selectById", Integer.class, SelectOptions.class);

  private static Method method1 =
      getDeclaredMethod(
          EmpDaoImpl.class,
          "selectByNameAndSalary",
          String.class,
          BigDecimal.class,
          SelectOptions.class);

  private static Method method2 = getDeclaredMethod(EmpDaoImpl.class, "selectByExample", Emp.class);

  private static Method method3 = getDeclaredMethod(EmpDaoImpl.class, "insert", Emp.class);

  private static Method method4 = getDeclaredMethod(EmpDaoImpl.class, "update", Emp.class);

  private static Method method5 = getDeclaredMethod(EmpDaoImpl.class, "delete", Emp.class);

  private static Method method6 =
      getDeclaredMethod(EmpDaoImpl.class, "iterate", IterationCallback.class);

  private static Method method7 = getDeclaredMethod(EmpDaoImpl.class, "execute");

  public EmpDaoImpl() {
    super(new ExampleConfig());
  }

  public EmpDaoImpl(Connection connection) {
    super(new ExampleConfig(), connection);
  }

  public EmpDaoImpl(DataSource dataSource) {
    super(new ExampleConfig(), dataSource);
  }

  @Override
  public Emp selectById(Integer id, SelectOptions option) {
    SqlFileSelectQuery query = getQueryImplementors().createSqlFileSelectQuery(method0);
    query.setConfig(__config);
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectById"));
    query.addParameter("id", Integer.class, id);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectById");
    query.prepare();
    SelectCommand<Emp> command =
        getCommandImplementors()
            .createSelectCommand(
                method0, query, new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions option) {
    SqlFileSelectQuery query = getQueryImplementors().createSqlFileSelectQuery(method1);
    query.setConfig(__config);
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectByNameAndSalary"));
    query.addParameter("name", String.class, name);
    query.addParameter("salary", BigDecimal.class, salary);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    SelectCommand<List<Emp>> command =
        getCommandImplementors()
            .createSelectCommand(
                method1, query, new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByExample(Emp emp) {
    SqlFileSelectQuery query = getQueryImplementors().createSqlFileSelectQuery(method2);
    query.setConfig(__config);
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectByNameAndSalary"));
    query.addParameter("emp", Emp.class, emp);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    SelectCommand<List<Emp>> command =
        getCommandImplementors()
            .createSelectCommand(
                method2, query, new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public int insert(Emp entity) {
    AutoInsertQuery<Emp> query =
        getQueryImplementors().createAutoInsertQuery(method3, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("insert");
    query.prepare();
    InsertCommand command = getCommandImplementors().createInsertCommand(method3, query);
    return command.execute();
  }

  @Override
  public int update(Emp entity) {
    AutoUpdateQuery<Emp> query =
        getQueryImplementors().createAutoUpdateQuery(method4, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("update");
    query.prepare();
    UpdateCommand command = getCommandImplementors().createUpdateCommand(method4, query);
    return command.execute();
  }

  @Override
  public int delete(Emp entity) {
    AutoDeleteQuery<Emp> query =
        getQueryImplementors().createAutoDeleteQuery(method5, _Emp.getSingletonInternal());
    query.setConfig(__config);
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("delete");
    query.prepare();
    DeleteCommand command = getCommandImplementors().createDeleteCommand(method5, query);
    return command.execute();
  }

  @Override
  public Integer stream(Function<Stream<Emp>, Integer> mapper) {
    SqlFileSelectQuery query = getQueryImplementors().createSqlFileSelectQuery(method6);
    query.setConfig(__config);
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "iterate"));
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("iterate");
    query.prepare();
    SelectCommand<Integer> command =
        getCommandImplementors()
            .createSelectCommand(
                method6,
                query,
                new EntityStreamHandler<Emp, Integer>(_Emp.getSingletonInternal(), mapper));
    return command.execute();
  }

  @Override
  public void execute() {
    SqlFileScriptQuery query = getQueryImplementors().createSqlFileScriptQuery(method7);
    query.setConfig(__config);
    query.setScriptFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "execute"));
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("execute");
    query.prepare();
    ScriptCommand command = getCommandImplementors().createScriptCommand(method7, query);
    command.execute();
  }
}
