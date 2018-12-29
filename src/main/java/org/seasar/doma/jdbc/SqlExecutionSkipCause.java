package org.seasar.doma.jdbc;

/** Defines the causes that skip the SQL execution. */
public enum SqlExecutionSkipCause {

  /** entity states are not changed in the update process */
  STATE_UNCHANGED,

  /** there is no entity in the batch process */
  BATCH_TARGET_NONEXISTENT
}
