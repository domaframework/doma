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
package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertOnDuplicateKeyUpdateSetValuesDeclaration;

public class NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet
    extends AbstractStatement<NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet, Integer> {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(
      Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  /**
   * Specify the set clause for the UPSERT statement. if no set-value are specified, the insert
   * value that exclude {@link org.seasar.doma.Id} properties are used for set value.
   *
   * @param block the consumer to set the clause
   * @return terminal statement
   */
  public NativeSqlUpsertTerminal set(
      Consumer<InsertOnDuplicateKeyUpdateSetValuesDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.upsertSetValues(block);
    return new NativeSqlUpsertTerminal(config, declaration);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlUpsertTerminal statement = new NativeSqlUpsertTerminal(config, declaration);
    return statement.createCommand();
  }
}
