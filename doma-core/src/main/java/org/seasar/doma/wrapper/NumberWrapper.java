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
package org.seasar.doma.wrapper;

/**
 * A wrapper for the {@link Number} class.
 *
 * @param <BASIC> The number type subclass
 */
public interface NumberWrapper<BASIC extends Number> extends Wrapper<BASIC> {

  @Override
  void set(Number value);

  /** Increments this object. */
  void increment();

  /** Decrements this object. */
  void decrement();
}
