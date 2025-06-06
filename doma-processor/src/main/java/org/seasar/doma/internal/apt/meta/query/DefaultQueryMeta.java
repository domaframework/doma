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
package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class DefaultQueryMeta extends AbstractQueryMeta {

  private final boolean isVirtual;

  public DefaultQueryMeta(
      TypeElement daoElement, ExecutableElement methodElement, boolean isVirtual) {
    super(daoElement, methodElement);
    this.isVirtual = isVirtual;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitDefaultQueryMeta(this);
  }

  public boolean isVirtual() {
    return isVirtual;
  }
}
