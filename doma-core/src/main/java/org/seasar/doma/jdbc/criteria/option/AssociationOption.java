package org.seasar.doma.jdbc.criteria.option;

public interface AssociationOption {
  enum Kind implements AssociationOption {
    MANDATORY,
    OPTIONAL
  }

  static AssociationOption mandatory() {
    return Kind.MANDATORY;
  }

  static AssociationOption optional() {
    return Kind.OPTIONAL;
  }
}
