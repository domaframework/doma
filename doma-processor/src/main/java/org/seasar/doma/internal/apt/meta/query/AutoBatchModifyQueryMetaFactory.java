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

import java.util.List;
import java.util.Objects;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class AutoBatchModifyQueryMetaFactory
    extends AbstractQueryMetaFactory<AutoBatchModifyQueryMeta> {

  private final BatchModifyAnnot batchModifyAnnot;
  private final QueryKind queryKind;

  public AutoBatchModifyQueryMetaFactory(
      RoundContext ctx,
      TypeElement daoElement,
      ExecutableElement methodElement,
      BatchModifyAnnot batchModifyAnnot,
      QueryKind queryKind) {
    super(ctx, daoElement, methodElement);
    this.batchModifyAnnot = Objects.requireNonNull(batchModifyAnnot);
    this.queryKind = Objects.requireNonNull(queryKind);
  }

  @Override
  public QueryMeta createQueryMeta(AnnotationMirror annotation) {
    AutoBatchModifyQueryMeta queryMeta = createAutoBatchModifyQueryMeta(annotation);
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  private AutoBatchModifyQueryMeta createAutoBatchModifyQueryMeta(AnnotationMirror annotation) {
    var queryMeta = new AutoBatchModifyQueryMeta(daoElement, methodElement);
    queryMeta.setBatchModifyAnnot(batchModifyAnnot);
    queryMeta.setQueryKind(queryKind);
    return queryMeta;
  }

  @Override
  protected void doReturnType(AutoBatchModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isBatchResult(entityCtType)) {
        throw new AptException(Message.DOMA4223, methodElement, new Object[] {});
      }
    } else {
      if (!returnMeta.isPrimitiveIntArray()) {
        throw new AptException(Message.DOMA4040, methodElement, new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoBatchModifyQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement, new Object[] {});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    IterableCtType iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4042, methodElement, new Object[] {});
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    EntityCtType entityCtType =
        iterableCtType
            .getElementCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4043, methodElement, new Object[] {});
                  }

                  @Override
                  public EntityCtType visitEntityCtType(EntityCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    queryMeta.setEntityCtType(entityCtType);
    queryMeta.setEntitiesParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    BatchModifyAnnot batchModifyAnnot = queryMeta.getBatchModifyAnnot();
    validateEntityPropertyNames(
        entityCtType.getType(),
        batchModifyAnnot.getAnnotationMirror(),
        batchModifyAnnot.getInclude(),
        batchModifyAnnot.getExclude(),
        batchModifyAnnot.getDuplicateKeys());
  }
}
