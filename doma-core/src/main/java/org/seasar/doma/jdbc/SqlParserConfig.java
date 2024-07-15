package org.seasar.doma.jdbc;

/** A configuration for SQL parser. */
public interface SqlParserConfig {

  /** The default implementation. */
  SqlParserConfig DEFAULT =
      new SqlParserConfig() {
        @Override
        public boolean shouldRemoveBlockComment(String comment) {
          return false;
        }

        @Override
        public boolean shouldRemoveLineComment(String comment) {
          return false;
        }
      };

  /**
   * Returns whether block comments should be removed.
   *
   * @param comment the block comment
   * @return whether block comments should be removed
   */
  boolean shouldRemoveBlockComment(String comment);

  /**
   * Returns whether line comments should be removed.
   *
   * @param comment the line comment
   * @return whether line comments should be removed
   */
  boolean shouldRemoveLineComment(String comment);
}
