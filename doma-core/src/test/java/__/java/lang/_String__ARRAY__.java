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
package __.java.lang;

import org.seasar.doma.wrapper.ObjectWrapper;

public final class _String__ARRAY__
    extends org.seasar.doma.jdbc.domain.AbstractDomainType<Object, String[]> {

  private static final _String__ARRAY__ singleton = new _String__ARRAY__();

  private _String__ARRAY__() {
    super(ObjectWrapper::new);
  }

  @Override
  public String[] newDomain(Object value) {
    return null;
  }

  @Override
  public Object getBasicValue(String[] domain) {
    return null;
  }

  @Override
  public Class<Object> getBasicClass() {
    return Object.class;
  }

  @Override
  public Class<String[]> getDomainClass() {
    return String[].class;
  }

  /**
   * @return the singleton
   */
  public static _String__ARRAY__ getSingletonInternal() {
    return singleton;
  }
}
