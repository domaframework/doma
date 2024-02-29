package org.seasar.doma.jdbc.query;

/** Assemble the upsert query interface. Implement this interface for each dialect. */
public interface UpsertAssembler {
  void assemble();
}
