package org.seasar.doma.jdbc;

/** Default values for {@link Config} objects. */
public final class ConfigSupport {

  public static SqlFileRepository defaultSqlFileRepository = new GreedyCacheSqlFileRepository();

  public static JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

  public static RequiresNewController defaultRequiresNewController = new RequiresNewController() {};

  public static ClassHelper defaultClassHelper = new ClassHelper() {};

  public static CommandImplementors defaultCommandImplementors = new CommandImplementors() {};

  public static QueryImplementors defaultQueryImplementors = new QueryImplementors() {};

  public static UnknownColumnHandler defaultUnknownColumnHandler = new UnknownColumnHandler() {};

  public static Naming defaultNaming = Naming.DEFAULT;

  public static MapKeyNaming defaultMapKeyNaming = new MapKeyNaming() {};

  public static Commenter defaultCommenter = new Commenter() {};

  public static EntityListenerProvider defaultEntityListenerProvider =
      new EntityListenerProvider() {};
}
