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
package org.seasar.doma.internal.expr;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Person {

  public String name;

  public static final String staticName = "hoge";

  public Optional<String> optionalName;

  public static final Optional<String> staticOptionalName = Optional.of("foo");

  public Optional<String> getOptionalName() {
    return optionalName;
  }

  @SuppressWarnings("SameReturnValue")
  public static Optional<String> getStaticOptionalName() {
    return staticOptionalName;
  }

  public OptionalInt age;

  public static OptionalInt staticAge;

  public OptionalInt getAge() {
    return age;
  }

  public static OptionalInt getStaticAge() {
    return staticAge;
  }

  public OptionalLong salary;

  public OptionalDouble temperature;
}
