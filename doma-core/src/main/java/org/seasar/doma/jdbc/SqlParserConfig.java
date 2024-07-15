package org.seasar.doma.jdbc;

/** A configuration for SQL parser. */
public interface SqlParserConfig {

  /** The default implementation. */
  SqlParserConfig DEFAULT =
      new SqlParserConfig() {
        @Override
        public boolean shouldRemoveBlockComments() {
          return false;
        }

        @Override
        public boolean shouldRemoveLineComments() {
          return false;
        }
      };

  /**
   * Returns whether block comments should be removed.
   *
   * @return whether block comments should be removed
   */
  boolean shouldRemoveBlockComments();

  /**
   * Returns whether line comments should be removed.
   *
   * @return whether line comments should be removed
   */
  boolean shouldRemoveLineComments();
}
