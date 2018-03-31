package org.seasar.doma;

/** Defines access levels in The Java Language. */
public enum AccessLevel {

  /** {@code public} */
  PUBLIC("public"),

  /** {@code protected} */
  PROTECTED("protected"),

  /** package private */
  PACKAGE("");

  private final String modifier;

  AccessLevel(String modifier) {
    this.modifier = modifier;
  }

  /** Returns the modifier's name. */
  public String getModifier() {
    return modifier;
  }
}
