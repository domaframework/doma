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
 * A skeletal implementation for the {@link Wrapper} interface .
 *
 * @param <BASIC> the basic type
 */
public abstract class AbstractWrapper<BASIC> implements Wrapper<BASIC> {

  protected final Class<BASIC> basicClass;

  protected BASIC value;

  protected AbstractWrapper(Class<BASIC> basicClass) {
    this.basicClass = basicClass;
  }

  protected AbstractWrapper(Class<BASIC> basicClass, BASIC value) {
    this(basicClass);
    doSet(value);
  }

  @Override
  public final void set(BASIC value) {
    doSet(value);
  }

  protected void doSet(BASIC value) {
    this.value = value;
  }

  @Override
  public final BASIC get() {
    return doGet();
  }

  protected BASIC doGet() {
    return value;
  }

  @Override
  public final BASIC getCopy() {
    return doGetCopy();
  }

  protected BASIC doGetCopy() {
    return doGet();
  }

  @Override
  public BASIC getDefault() {
    return null;
  }

  @Override
  public final boolean hasEqualValue(Object otherValue) {
    return doHasEqualValue(otherValue);
  }

  protected boolean doHasEqualValue(Object otherValue) {
    BASIC value = doGet();
    if (value == null) {
      return otherValue == null;
    }
    return value.equals(otherValue);
  }

  @Override
  public Class<BASIC> getBasicClass() {
    return basicClass;
  }

  @Override
  public String toString() {
    return value == null ? "null" : value.toString();
  }
}
