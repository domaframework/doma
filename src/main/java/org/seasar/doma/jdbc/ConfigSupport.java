package org.seasar.doma.jdbc;

/** Default values for {@link Config} objects. */
public final class ConfigSupport {

  public static final SqlFileRepository defaultSqlFileRepository =
      new GreedyCacheSqlFileRepository();

  public static final JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

  public static final RequiresNewController defaultRequiresNewController =
      new RequiresNewController() {};

  public static final ClassHelper defaultClassHelper = new ClassHelper() {};

  public static final CommandImplementors defaultCommandImplementors = new CommandImplementors() {};

  public static final QueryImplementors defaultQueryImplementors = new QueryImplementors() {};

  public static final UnknownColumnHandler defaultUnknownColumnHandler =
      new UnknownColumnHandler() {};

  public static final Naming defaultNaming = Naming.DEFAULT;

  public static final MapKeyNaming defaultMapKeyNaming = new MapKeyNaming() {};

  public static final Commenter defaultCommenter = new Commenter() {};

  public static final EntityListenerProvider defaultEntityListenerProvider =
      new EntityListenerProvider() {};
}
