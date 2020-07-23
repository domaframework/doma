package org.seasar.doma.jdbc.criteria.option;

public interface DistinctOption {
  enum Kind implements DistinctOption {
    NONE,
    BASIC
  }

  static DistinctOption none() {
    return Kind.NONE;
  }

  static DistinctOption basic() {
    return Kind.BASIC;
  }
}
