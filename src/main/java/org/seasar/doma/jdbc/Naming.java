package org.seasar.doma.jdbc;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

/** A naming convention controller. */
public interface Naming {

  /** the adapter for {@link NamingType#NONE} */
  static Naming NONE = new Adapter(NamingType.NONE);

  /** the adapter for {@link NamingType#LOWER_CASE} */
  static Naming LOWER_CASE = new Adapter(NamingType.LOWER_CASE);

  /** the adapter for {@link NamingType#UPPER_CASE} */
  static Naming UPPER_CASE = new Adapter(NamingType.UPPER_CASE);

  /** the adapter for {@link NamingType#SNAKE_LOWER_CASE} */
  static Naming SNAKE_LOWER_CASE = new Adapter(NamingType.SNAKE_LOWER_CASE);

  /** the adapter for {@link NamingType#SNAKE_UPPER_CASE} */
  static Naming SNAKE_UPPER_CASE = new Adapter(NamingType.SNAKE_UPPER_CASE);

  /** the adapter for {@link NamingType#LENIENT_SNAKE_LOWER_CASE} */
  static Naming LENIENT_SNAKE_LOWER_CASE = new Adapter(NamingType.LENIENT_SNAKE_LOWER_CASE);

  /** the adapter for {@link NamingType#LENIENT_SNAKE_UPPER_CASE} */
  static Naming LENIENT_SNAKE_UPPER_CASE = new Adapter(NamingType.LENIENT_SNAKE_UPPER_CASE);

  /** the default convention */
  static Naming DEFAULT = NONE;

  /**
   * Applies the naming convention.
   *
   * @param namingType the naming convention. If this parameter is {@code null}, {@link
   *     Entity#naming()} does not have explicit value.
   * @param text the text
   * @return the converted text
   * @throws DomaNullPointerException if {@code text} is {@code null}
   */
  String apply(NamingType namingType, String text);

  /**
   * Reverts the text to the original as much as possible.
   *
   * @param namingType the naming convention. If this parameter is {@code null}, {@link
   *     Entity#naming()} does not have explicit value.
   * @param text the text that is converted by this convention
   * @return the original
   * @throws DomaNullPointerException if {@code text} is {@code null}
   */
  String revert(NamingType namingType, String text);

  class Adapter implements Naming {

    protected final NamingType namingType;

    private Adapter(NamingType namingType) {
      if (namingType == null) {
        throw new DomaNullPointerException("namingType");
      }
      this.namingType = namingType;
    }

    @Override
    public String apply(NamingType namingType, String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      if (namingType == null) {
        return this.namingType.apply(text);
      }
      return namingType.apply(text);
    }

    @Override
    public String revert(NamingType namingType, String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      if (namingType == null) {
        return this.namingType.revert(text);
      }
      return namingType.revert(text);
    }
  }
}
