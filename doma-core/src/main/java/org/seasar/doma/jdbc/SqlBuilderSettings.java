package org.seasar.doma.jdbc;

/** A context for SQL builder settings. */
public interface SqlBuilderSettings {

  /**
   * Returns whether the block comment should be removed. The beginning of the {@code comment}
   * parameter string must always start with "/*".
   *
   * @param comment the block comment, never null
   * @return true if the block comment should be removed
   */
  default boolean shouldRemoveBlockComment(String comment) {
    return false;
  }

  /**
   * Returns whether the line comment should be removed. The beginning of the {@code comment}
   * parameter string must always start with "--".
   *
   * @param comment the line comment, never null
   * @return true if the line comment should be removed
   */
  default boolean shouldRemoveLineComment(String comment) {
    return false;
  }

  /**
   * Returns whether the blank lines should be removed.
   *
   * @return true if the blank lines should be removed
   */
  default boolean shouldRemoveBlankLines() {
    return false;
  }

  /**
   * Indicates whether padding is required for the "IN" list clauses in SQL generation.
   *
   * @return {@code true} if padding is required; {@code false} otherwise
   */
  default boolean requiresInListPadding() {
    return false;
  }
}
