package org.seasar.doma.jdbc.tx;

/** Defines transaction propagation behaviors. */
public enum TransactionAttribute {

  /** Support a current transaction, create a new one if none exists. */
  REQUIRED,

  /** Create a new transaction, and suspend the current transaction if one exists. */
  REQUIRES_NEW,

  /** Execute non-transactionally, and suspend the current transaction if one exists. */
  NOT_SUPPORTED
}
