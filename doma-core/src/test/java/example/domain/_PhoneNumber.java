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
package example.domain;

import org.seasar.doma.jdbc.domain.AbstractDomainType;
import org.seasar.doma.wrapper.StringWrapper;

public class _PhoneNumber extends AbstractDomainType<String, PhoneNumber> {

  private static final _PhoneNumber singleton = new _PhoneNumber();

  private _PhoneNumber() {
    super(StringWrapper::new);
  }

  @Override
  public PhoneNumber newDomain(String value) {
    return new PhoneNumber(value);
  }

  @Override
  public String getBasicValue(PhoneNumber domain) {
    if (domain == null) {
      return null;
    }
    return domain.getValue();
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<PhoneNumber> getDomainClass() {
    return PhoneNumber.class;
  }

  public static _PhoneNumber getSingletonInternal() {
    return singleton;
  }
}
