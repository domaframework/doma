package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/** A generator that uses a database TABLE. */
public interface TableIdGenerator extends IdGenerator {

  /**
   * Sets the qualified name of the table.
   *
   * @param qualifiedTableName the qualified name of the table
   */
  void setQualifiedTableName(String qualifiedTableName);

  /**
   * Sets the initial value.
   *
   * @param initialValue the initial value
   */
  void setInitialValue(long initialValue);

  /**
   * Sets the allocation size.
   *
   * @param allocationSize the allocation size
   */
  void setAllocationSize(long allocationSize);

  /**
   * Sets the column name of the primary key.
   *
   * @param pkColumnName the column name of the primary key
   */
  void setPkColumnName(String pkColumnName);

  /**
   * Sets the column value of the primary key.
   *
   * @param pkColumnValue the column value of the primary key
   */
  void setPkColumnValue(String pkColumnValue);

  /**
   * Sets the column name of the identity value.
   *
   * @param valueColumnName the column name of the identity value
   */
  void setValueColumnName(String valueColumnName);

  /**
   * Initializes this generator.
   *
   * @throws JdbcException if the initialization is failed
   */
  void initialize();
}
