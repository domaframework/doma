/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc;

import java.util.Objects;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

/** A naming convention controller. */
public interface Naming {

  /** the adapter for {@link NamingType#NONE} */
  Naming NONE = new Adapter(NamingType.NONE);

  /** the adapter for {@link NamingType#LOWER_CASE} */
  Naming LOWER_CASE = new Adapter(NamingType.LOWER_CASE);

  /** the adapter for {@link NamingType#UPPER_CASE} */
  Naming UPPER_CASE = new Adapter(NamingType.UPPER_CASE);

  /** the adapter for {@link NamingType#SNAKE_LOWER_CASE} */
  Naming SNAKE_LOWER_CASE = new Adapter(NamingType.SNAKE_LOWER_CASE);

  /** the adapter for {@link NamingType#SNAKE_UPPER_CASE} */
  Naming SNAKE_UPPER_CASE = new Adapter(NamingType.SNAKE_UPPER_CASE);

  /** the default convention */
  Naming DEFAULT = NONE;

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
      return Objects.requireNonNullElse(namingType, this.namingType).apply(text);
    }

    @Override
    public String revert(NamingType namingType, String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return Objects.requireNonNullElse(namingType, this.namingType).revert(text);
    }
  }
}
