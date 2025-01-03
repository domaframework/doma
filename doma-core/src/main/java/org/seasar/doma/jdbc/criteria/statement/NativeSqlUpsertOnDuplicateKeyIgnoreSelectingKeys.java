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

import java.util.Arrays;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys
    extends AbstractStatement<NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys, Integer> {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(
      Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
    this.declaration.getContext().onDuplicateContext.duplicateKeyType = DuplicateKeyType.IGNORE;
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement. if no keys are specified, the
   * {@link org.seasar.doma.Id} property are used for duplicate checking.
   *
   * @param keys keys the keys used for duplicate checking
   * @return selecting set statement builder
   */
  public NativeSqlUpsertTerminal keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    InsertContext context = this.declaration.getContext();
    context.onDuplicateContext.keys = Arrays.asList(keys);
    return new NativeSqlUpsertTerminal(config, declaration);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlUpsertTerminal statement = new NativeSqlUpsertTerminal(config, declaration);
    return statement.createCommand();
  }
}
