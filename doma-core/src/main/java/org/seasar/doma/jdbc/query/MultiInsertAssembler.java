package org.seasar.doma.jdbc.query;

/** Assemble the multi-row insert query interface. Implement this interface for each dialect. */
public interface MultiInsertAssembler {
  void assemble();
}
