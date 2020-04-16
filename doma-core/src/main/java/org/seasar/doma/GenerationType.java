package org.seasar.doma;

/**
 * Defines strategies to generate identifiers.
 *
 * @see GeneratedValue
 */
public enum GenerationType {

  /** Database IDENTITY column */
  IDENTITY,

  /** Database sequence */
  SEQUENCE,

  /** Database table */
  TABLE
}
