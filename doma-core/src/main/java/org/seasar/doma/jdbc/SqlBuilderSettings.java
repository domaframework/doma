package org.seasar.doma.jdbc;

public interface SqlBuilderSettings {
  default boolean shouldRemoveEmptyLines() {
    return false;
  }
}
