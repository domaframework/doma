package org.seasar.doma.jdbc;

/**
 * An accessor for {@link SelectOptions}.
 *
 * <p>This is used only by DOMA framework.
 */
public class SelectOptionsAccessor {

  public static boolean isCount(SelectOptions options) {
    return options.count;
  }

  public static void setCountSize(SelectOptions options, long countSize) {
    options.countSize = countSize;
  }

  public static SelectForUpdateType getForUpdateType(SelectOptions options) {
    return options.forUpdateType;
  }

  public static int getWaitSeconds(SelectOptions options) {
    return options.waitSeconds;
  }

  public static String[] getAliases(SelectOptions options) {
    return options.aliases;
  }

  public static long getOffset(SelectOptions options) {
    return options.offset;
  }

  public static long getLimit(SelectOptions options) {
    return options.limit;
  }
}
