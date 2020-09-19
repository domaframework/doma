package example.dao;

import example.entity.Emp;
import example.entity._Emp;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Generated;
import javax.sql.DataSource;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.EntityStreamHandler;
import org.seasar.doma.internal.jdbc.dao.DaoImplSupport;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigProvider;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.command.ScriptCommand;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.SqlFileScriptQuery;
import org.seasar.doma.jdbc.query.SqlFileSelectQuery;

@Generated("")
public class EmpDaoImpl implements EmpDao, ConfigProvider {

  private static final Method method0 =
      DaoImplSupport.getDeclaredMethod(
          EmpDaoImpl.class, "selectById", Integer.class, SelectOptions.class);

  private static final Method method1 =
      DaoImplSupport.getDeclaredMethod(
          EmpDaoImpl.class,
          "selectByNameAndSalary",
          String.class,
          BigDecimal.class,
          SelectOptions.class);

  private static final Method method2 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "selectByExample", Emp.class);

  private static final Method method3 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "insert", Emp.class);

  private static final Method method4 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "update", Emp.class);

  private static final Method method5 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "delete", Emp.class);

  private static final Method method6 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "iterate", IterationCallback.class);

  private static final Method method7 =
      DaoImplSupport.getDeclaredMethod(EmpDaoImpl.class, "execute");

  private final DaoImplSupport __support;

  public EmpDaoImpl() {
    __support = new DaoImplSupport(new ExampleConfig());
  }

  public EmpDaoImpl(Connection connection) {
    __support = new DaoImplSupport(new ExampleConfig(), connection);
  }

  public EmpDaoImpl(DataSource dataSource) {
    __support = new DaoImplSupport(new ExampleConfig(), dataSource);
  }

  @Override
  public Config getConfig() {
    return __support.getConfig();
  }

  @Override
  public Emp selectById(Integer id, SelectOptions option) {
    SqlFileSelectQuery query = __support.getQueryImplementors().createSqlFileSelectQuery(method0);
    query.setConfig(__support.getConfig());
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectById"));
    query.addParameter("id", Integer.class, id);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectById");
    query.prepare();
    SelectCommand<Emp> command =
        __support
            .getCommandImplementors()
            .createSelectCommand(
                method0, query, new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions option) {
    SqlFileSelectQuery query = __support.getQueryImplementors().createSqlFileSelectQuery(method1);
    query.setConfig(__support.getConfig());
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectByNameAndSalary"));
    query.addParameter("name", String.class, name);
    query.addParameter("salary", BigDecimal.class, salary);
    query.setOptions(option);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    SelectCommand<List<Emp>> command =
        __support
            .getCommandImplementors()
            .createSelectCommand(
                method1, query, new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public List<Emp> selectByExample(Emp emp) {
    SqlFileSelectQuery query = __support.getQueryImplementors().createSqlFileSelectQuery(method2);
    query.setConfig(__support.getConfig());
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "selectByNameAndSalary"));
    query.addParameter("emp", Emp.class, emp);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("selectByNameAndSalary");
    query.prepare();
    SelectCommand<List<Emp>> command =
        __support
            .getCommandImplementors()
            .createSelectCommand(
                method2, query, new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
    return command.execute();
  }

  @Override
  public int insert(Emp entity) {
    AutoInsertQuery<Emp> query =
        __support
            .getQueryImplementors()
            .createAutoInsertQuery(method3, _Emp.getSingletonInternal());
    query.setConfig(__support.getConfig());
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("insert");
    query.prepare();
    InsertCommand command = __support.getCommandImplementors().createInsertCommand(method3, query);
    return command.execute();
  }

  @Override
  public int update(Emp entity) {
    AutoUpdateQuery<Emp> query =
        __support
            .getQueryImplementors()
            .createAutoUpdateQuery(method4, _Emp.getSingletonInternal());
    query.setConfig(__support.getConfig());
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("update");
    query.prepare();
    UpdateCommand command = __support.getCommandImplementors().createUpdateCommand(method4, query);
    return command.execute();
  }

  @Override
  public int delete(Emp entity) {
    AutoDeleteQuery<Emp> query =
        __support
            .getQueryImplementors()
            .createAutoDeleteQuery(method5, _Emp.getSingletonInternal());
    query.setConfig(__support.getConfig());
    query.setEntity(entity);
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("delete");
    query.prepare();
    DeleteCommand command = __support.getCommandImplementors().createDeleteCommand(method5, query);
    return command.execute();
  }

  @Override
  public Integer stream(Function<Stream<Emp>, Integer> mapper) {
    SqlFileSelectQuery query = __support.getQueryImplementors().createSqlFileSelectQuery(method6);
    query.setConfig(__support.getConfig());
    query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "iterate"));
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("iterate");
    query.prepare();
    SelectCommand<Integer> command =
        __support
            .getCommandImplementors()
            .createSelectCommand(
                method6,
                query,
                new EntityStreamHandler<Emp, Integer>(_Emp.getSingletonInternal(), mapper));
    return command.execute();
  }

  @Override
  public void execute() {
    SqlFileScriptQuery query = __support.getQueryImplementors().createSqlFileScriptQuery(method7);
    query.setConfig(__support.getConfig());
    query.setScriptFilePath(SqlFileUtil.buildPath("example.dao.EmpDao", "execute"));
    query.setCallerClassName("example.dao.EmpDao");
    query.setCallerMethodName("execute");
    query.prepare();
    ScriptCommand command = __support.getCommandImplementors().createScriptCommand(method7, query);
    command.execute();
  }
}
