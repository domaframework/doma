package org.seasar.doma.jdbc.criteria.option;

/** Represents the option about the associate and associateWith operators. */
public interface AssociationOption {
  enum Kind implements AssociationOption {
    MANDATORY,
    OPTIONAL
  }

  /**
   * Indicates that the related join operation is mandatory.
   *
   * @return the kind
   */
  static AssociationOption mandatory() {
    return Kind.MANDATORY;
  }

  /**
   * Indicates that the related join operation is optional.
   *
   * @return the kind
   */
  static AssociationOption optional() {
    return Kind.OPTIONAL;
  }
}
