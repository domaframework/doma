package org.seasar.doma.jdbc;

public interface SqlBuilderSettings {

  default boolean shouldRemoveBlockComment(String comment) {
    return false;
  }

  default boolean shouldRemoveLineComment(String comment) {
    return false;
  }

  default boolean shouldRemoveBlankLines() {
    return false;
  }
}
