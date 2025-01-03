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
import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;

public class NativeSqlUpdateStarting {

  private final Config config;
  private final UpdateDeclaration declaration;

  public NativeSqlUpdateStarting(Config config, UpdateDeclaration declaration) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(declaration);
    this.config = config;
    this.declaration = declaration;
  }

  public NativeSqlUpdateTerminal set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.set(block);
    return new NativeSqlUpdateTerminal(config, declaration);
  }
}
