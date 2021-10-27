package org.seasar.doma.it;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.seasar.doma.it.dao.ScriptDao;
import org.seasar.doma.it.dao.ScriptDaoImpl;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.HsqldbDialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.SqliteDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegrationTestEnvironment
    implements BeforeAllCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        AfterAllCallback,
        ParameterResolver,
        ExecutionCondition {

  private static final Logger logger = LoggerFactory.getLogger(IntegrationTestEnvironment.class);

  private static transient boolean imported;

  private final Dbms dbms;

  private final AppConfig config;

  private final ScriptDao scriptDao;

  public IntegrationTestEnvironment() {
    String driver = System.getProperty("driver");
    logger.debug("driver={}", Objects.requireNonNull(driver));
    String url = System.getProperty("url");
    logger.debug("url={}", Objects.requireNonNull(url));
    dbms = determineDbms(driver);
    Dialect dialect = createDialect(dbms);
    config = new AppConfig(dialect, dbms, url, "test", "test");
    scriptDao = new ScriptDaoImpl(config);
  }

  private static Dbms determineDbms(String driver) {
    switch (driver) {
      case "h2":
        return Dbms.H2;
      case "mysql":
        return Dbms.MYSQL;
      case "oracle":
        return Dbms.ORACLE;
      case "postgresql":
        return Dbms.POSTGRESQL;
      case "sqlserver":
        return Dbms.SQLSERVER;
      default:
        throw new IllegalArgumentException(driver);
    }
  }

  private static Dialect createDialect(Dbms dbms) {
    switch (dbms) {
      case H2:
        return new H2Dialect();
      case HSQLDB:
        return new HsqldbDialect();
      case SQLITE:
        return new SqliteDialect();
      case MYSQL:
        return new MysqlDialect();
      case POSTGRESQL:
        return new PostgresDialect();
      case SQLSERVER:
        return new MssqlDialect();
      case ORACLE:
        return new OracleDialect();
      case DB2:
        return new Db2Dialect();
    }
    throw new IllegalArgumentException("unreachable: " + dbms);
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    if (imported) {
      return;
    }
    config
        .getTransactionManager()
        .required(
            () -> {
              try {
                scriptDao.drop();
              } catch (ScriptException ignored) {
              }
            });
    config.getTransactionManager().required(scriptDao::create);
    imported = true;
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    config.getLocalTransaction().begin();
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    config.getLocalTransaction().rollback();
  }

  @Override
  public void afterAll(ExtensionContext context) {}

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType() == Config.class;
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return config;
  }

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    Optional<Run> optional = findAnnotation(context.getElement(), Run.class);
    if (!optional.isPresent()) {
      return enabled("@Run is not present");
    }
    Run run = optional.get();
    if (isRunnable(run, dbms)) {
      return enabled("runnable: " + dbms);
    }
    return disabled("not runnable");
  }

  private boolean isRunnable(Run run, Dbms dbms) {
    List<Dbms> onlyIf = Arrays.asList(run.onlyIf());
    List<Dbms> unless = Arrays.asList(run.unless());

    if (onlyIf.size() > 0) {
      return onlyIf.contains(dbms);
    } else {
      if (unless.size() > 0) {
        return !unless.contains(dbms);
      } else {
        return true;
      }
    }
  }
}
