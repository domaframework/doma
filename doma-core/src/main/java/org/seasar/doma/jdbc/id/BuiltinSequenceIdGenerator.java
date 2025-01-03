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
package org.seasar.doma.jdbc.id;

import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.Sql;

/** The built-in implementation for {@link SequenceIdGenerator}. */
public class BuiltinSequenceIdGenerator extends AbstractPreGenerateIdGenerator
    implements SequenceIdGenerator {

  protected String qualifiedSequenceName;

  @Override
  public void setQualifiedSequenceName(String qualifiedSequenceName) {
    this.qualifiedSequenceName = qualifiedSequenceName;
  }

  @Override
  public void initialize() {}

  @Override
  protected long getNewInitialValue(IdGenerationConfig config) {
    Sql<?> sql = config.getDialect().getSequenceNextValSql(qualifiedSequenceName, allocationSize);
    return getGeneratedValue(config, sql);
  }

  @Override
  public GenerationType getGenerationType() {
    return GenerationType.SEQUENCE;
  }
}
