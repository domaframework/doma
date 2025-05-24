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
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory<AutoModifyQueryMeta> {

  private final ModifyAnnot modifyAnnot;
  private final QueryKind queryKind;

  public AutoModifyQueryMetaFactory(
      RoundContext ctx,
      TypeElement daoElement,
      ExecutableElement methodElement,
      ModifyAnnot modifyAnnot,
      QueryKind queryKind) {
    super(ctx, daoElement, methodElement);
    this.modifyAnnot = Objects.requireNonNull(modifyAnnot);
    this.queryKind = Objects.requireNonNull(queryKind);
  }

  @Override
  public QueryMeta createQueryMeta(AnnotationMirror annotation) {
    AutoModifyQueryMeta queryMeta = createAutoModifyQueryMeta(annotation);
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  private AutoModifyQueryMeta createAutoModifyQueryMeta(AnnotationMirror annotation) {
    var queryMeta = new AutoModifyQueryMeta(daoElement, methodElement);
    queryMeta.setModifyAnnot(modifyAnnot);
    queryMeta.setQueryKind(queryKind);
    return queryMeta;
  }

  @Override
  protected void doReturnType(AutoModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (queryMeta.getReturningAnnot() == null) {
      if (entityCtType != null && entityCtType.isImmutable()) {
        if (!returnMeta.isResult(entityCtType)) {
          throw new AptException(Message.DOMA4222, methodElement, new Object[] {});
        }
      } else {
        if (!returnMeta.isPrimitiveInt()) {
          throw new AptException(Message.DOMA4001, methodElement, new Object[] {});
        }
      }
    } else {
      if (!returnMeta.isEntity(entityCtType) && !returnMeta.isEntityOptional(entityCtType)) {
        throw new AptException(Message.DOMA4495, methodElement, new Object[] {});
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoModifyQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement, new Object[] {});
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    EntityCtType entityCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType type, Void p)
                      throws RuntimeException {
                    throw new AptException(
                        Message.DOMA4003, parameterMeta.getElement(), new Object[] {});
                  }

                  @Override
                  public EntityCtType visitEntityCtType(EntityCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    queryMeta.setEntityCtType(entityCtType);
    queryMeta.setEntityParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    ModifyAnnot modifyAnnot = queryMeta.getModifyAnnot();
    validateEntityPropertyNames(
        entityCtType.getType(),
        modifyAnnot.getAnnotationMirror(),
        modifyAnnot.getInclude(),
        modifyAnnot.getExclude(),
        modifyAnnot.getDuplicateKeys());

    var returningAnnot = queryMeta.getReturningAnnot();
    if (returningAnnot != null) {
      var returningAnnotValidator =
          new ReturningAnnotValidator(ctx, entityCtType.getType(), methodElement, returningAnnot);
      returningAnnotValidator.validate();
    }
  }
}
