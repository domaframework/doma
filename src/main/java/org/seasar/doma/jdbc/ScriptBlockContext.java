package org.seasar.doma.jdbc;

/** A context that corresponds to an SQL block in a script file. */
public interface ScriptBlockContext {

  /**
   * Adds the SQL keyword.
   *
   * @param keyword the SQL keyword
   */
  void addKeyword(String keyword);

  /**
   * Whether this context is in an SQL block.
   *
   * @return {@code true} if this context is in an SQL block
   */
  boolean isInBlock();
}
