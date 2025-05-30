/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma;

/**
 * Defines access levels in the Java language.
 *
 * <p>This enum is used to specify the visibility of generated classes in the Doma framework.
 */
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

  /**
   * @return the modifier's name
   */
  public String getModifier() {
    return modifier;
  }
}
