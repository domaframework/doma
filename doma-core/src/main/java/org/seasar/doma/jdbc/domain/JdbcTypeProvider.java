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
package org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.type.JdbcType;

/**
 * A JDBC type provider for a domain type.
 *
 * @param <DOMAIN> the domain type
 */
public abstract class JdbcTypeProvider<DOMAIN> implements DomainConverter<DOMAIN, Object> {

  @Override
  public final Object fromDomainToValue(DOMAIN domain) {
    return domain;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final DOMAIN fromValueToDomain(Object value) {
    return (DOMAIN) value;
  }

  /**
   * Returns the JDBC type.
   *
   * @return the JDBC type
   */
  public abstract JdbcType<DOMAIN> getJdbcType();
}
