package org.seasar.doma.jdbc.criteria.option;

/** Represents the option that decides whether to append the DISTINCT keyword or not. */
@SuppressWarnings("SameReturnValue")
public interface DistinctOption {
  enum Kind implements DistinctOption {
    NONE,
    BASIC
  }

  /**
   * Indicates that the DISTINCT keyword is not required.
   *
   * @return the kind
   */
  static DistinctOption none() {
    return Kind.NONE;
  }

  /**
   * Indicates that the DISTINCT keyword is required.
   *
   * @return the kind
   */
  static DistinctOption basic() {
    return Kind.BASIC;
  }
}
