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
package org.seasar.doma.internal.apt.validator;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "PERSON_ID")
  Integer id;

  Optional<String> name;

  static Optional<String> staticName;

  public Optional<String> getName() {
    return name;
  }

  public static Optional<String> getStaticName() {
    return staticName;
  }

  OptionalInt age;

  static OptionalInt staticAge;

  public OptionalInt getAge() {
    return age;
  }

  public static OptionalInt getStaticAge() {
    return staticAge;
  }

  OptionalLong salary;

  OptionalDouble temperature;
}
