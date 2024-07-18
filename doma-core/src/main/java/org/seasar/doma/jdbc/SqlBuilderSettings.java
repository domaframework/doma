package org.seasar.doma.jdbc;

public interface SqlBuilderSettings {
  default boolean shouldRemoveBlankLines() {
    return false;
  }
}
