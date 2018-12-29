package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/** A generator that uses a database SEQUENCE. */
public interface SequenceIdGenerator extends IdGenerator {

  /**
   * Sets the qualified name of the sequence.
   *
   * @param qualifiedSequenceName the qualified name of the sequence
   */
  void setQualifiedSequenceName(String qualifiedSequenceName);

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
   * Initializes this generator.
   *
   * @throws JdbcException if the initialization is failed
   */
  void initialize();
}
