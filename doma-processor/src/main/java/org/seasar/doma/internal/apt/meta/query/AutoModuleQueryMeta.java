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

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.meta.parameter.CallableSqlParameterMeta;

public abstract class AutoModuleQueryMeta extends AbstractQueryMeta {

  private final List<CallableSqlParameterMeta> sqlParameterMetas = new ArrayList<>();

  AutoModuleQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public void addCallableSqlParameterMeta(CallableSqlParameterMeta sqlParameterMeta) {
    sqlParameterMetas.add(sqlParameterMeta);
  }

  public List<CallableSqlParameterMeta> getCallableSqlParameterMetas() {
    return sqlParameterMetas;
  }

  public abstract MapKeyNamingType getMapKeyNamingType();
}
