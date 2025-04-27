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

import java.util.Objects;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.ReturningAnnot;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyNameCollector;
import org.seasar.doma.message.Message;

public class ReturningAnnotValidator {
  private final RoundContext ctx;
  private final TypeMirror entityType;
  private final ExecutableElement method;
  private final ReturningAnnot returningAnnot;

  public ReturningAnnotValidator(
      RoundContext ctx,
      TypeMirror entityType,
      ExecutableElement method,
      ReturningAnnot returningAnnot) {
    this.ctx = Objects.requireNonNull(ctx);
    this.entityType = Objects.requireNonNull(entityType);
    this.method = Objects.requireNonNull(method);
    this.returningAnnot = Objects.requireNonNull(returningAnnot);
  }

  void validate() {
    var inclusion = returningAnnot.getIncludeValue();
    var exclusion = returningAnnot.getExcludeValue();
    if (inclusion.isEmpty() && exclusion.isEmpty()) {
      return;
    }
    var collector = new EntityPropertyNameCollector(ctx);
    var names = collector.collect(entityType);
    for (var value : inclusion) {
      if (!names.contains(value)) {
        throw new AptException(
            Message.DOMA4493,
            method,
            returningAnnot.getAnnotationMirror(),
            returningAnnot.getInclude(),
            new Object[] {value, entityType});
      }
    }
    for (var value : exclusion) {
      if (!names.contains(value)) {
        throw new AptException(
            Message.DOMA4494,
            method,
            returningAnnot.getAnnotationMirror(),
            returningAnnot.getExclude(),
            new Object[] {value, entityType});
      }
    }
  }
}
