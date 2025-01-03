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
import org.seasar.doma.wrapper.IntegerWrapper;

public class _JobType extends AbstractDomainType<Integer, JobType> {

  private static final _JobType singleton = new _JobType();

  private _JobType() {
    super(IntegerWrapper::new);
  }

  @Override
  public JobType newDomain(Integer value) {
    if (value == null) {
      return new JobType(0);
    }
    return new JobType(value);
  }

  @Override
  public Integer getBasicValue(JobType domain) {
    return domain.getValue();
  }

  @Override
  public Class<Integer> getBasicClass() {
    return Integer.class;
  }

  @Override
  public Class<JobType> getDomainClass() {
    return JobType.class;
  }

  public static _JobType getSingletonInternal() {
    return singleton;
  }
}
