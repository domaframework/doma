package org.seasar.doma.jdbc;

/**
 * {@link Config} が返すインタフェースのデフォルト実装を提供します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public final class ConfigSupport {

  public static SqlFileRepository defaultSqlFileRepository = new GreedyCacheSqlFileRepository();

  public static JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

  public static RequiresNewController defaultRequiresNewController = new RequiresNewController() {};

  public static ClassHelper defaultClassHelper = new ClassHelper() {};

  public static CommandImplementors defaultCommandImplementors = new CommandImplementors() {};

  public static QueryImplementors defaultQueryImplementors = new QueryImplementors() {};

  public static UnknownColumnHandler defaultUnknownColumnHandler = new UnknownColumnHandler() {};

  /** @since 2.2.0 */
  public static Naming defaultNaming = Naming.DEFAULT;

  public static MapKeyNaming defaultMapKeyNaming = new MapKeyNaming() {};

  /** @since 2.1.0 */
  public static Commenter defaultCommenter = new Commenter() {};

  /** @since 2.2.0 */
  public static EntityListenerProvider defaultEntityListenerProvider =
      new EntityListenerProvider() {};
}
