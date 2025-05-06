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
package org.seasar.doma.jdbc.query;

import org.seasar.doma.jdbc.Sql;

/**
 * An abstract base class for queries that create database resources.
 *
 * <p>This class provides a skeletal implementation of the {@link CreateQuery} interface, reducing
 * the effort required to implement resource creation queries.
 *
 * @param <RESULT> the type of the resource to be created
 */
public abstract class AbstractCreateQuery<RESULT> extends AbstractQuery
    implements CreateQuery<RESULT> {

  /** {@inheritDoc} */
  @Override
  public int getQueryTimeout() {
    return -1;
  }

  /** {@inheritDoc} */
  @Override
  public Sql<?> getSql() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void complete() {}
}
