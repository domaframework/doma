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
package __.org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.domain.Job;
import org.seasar.doma.wrapper.StringWrapper;

public final class _Job extends org.seasar.doma.jdbc.domain.AbstractDomainType<String, Job> {

  private static final _Job singleton = new _Job();

  private _Job() {
    super(StringWrapper::new);
  }

  @Override
  public Job newDomain(String value) {
    return null;
  }

  @Override
  public String getBasicValue(Job domain) {
    return null;
  }

  @Override
  public Class<String> getBasicClass() {
    return String.class;
  }

  @Override
  public Class<Job> getDomainClass() {
    return Job.class;
  }

  /**
   * @return the singleton
   */
  public static _Job getSingletonInternal() {
    return singleton;
  }
}
