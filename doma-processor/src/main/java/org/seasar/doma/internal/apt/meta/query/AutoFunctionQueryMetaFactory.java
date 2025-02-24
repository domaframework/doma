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
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.annot.FunctionAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.parameter.*;
import org.seasar.doma.message.Message;

public class AutoFunctionQueryMetaFactory
    extends AutoModuleQueryMetaFactory<AutoFunctionQueryMeta> {

  public AutoFunctionQueryMetaFactory(
      RoundContext ctx, TypeElement daoElement, ExecutableElement methodElement) {
    super(ctx, daoElement, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    FunctionAnnot functionAnnot = ctx.getAnnotations().newFunctionAnnot(methodElement);
    if (functionAnnot == null) {
      return null;
    }
    AutoFunctionQueryMeta queryMeta = new AutoFunctionQueryMeta(daoElement, methodElement);
    queryMeta.setFunctionAnnot(functionAnnot);
    queryMeta.setQueryKind(QueryKind.AUTO_FUNCTION);
    doTypeParameters(queryMeta);
    doReturnType(queryMeta);
    doParameters(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  @Override
  protected void doReturnType(AutoFunctionQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(returnMeta);
    ResultParameterMeta resultParameterMeta = createResultParameterMeta(queryMeta, returnMeta);
    queryMeta.setResultParameterMeta(resultParameterMeta);
  }

  private ResultParameterMeta createResultParameterMeta(
      final AutoFunctionQueryMeta queryMeta, final QueryReturnMeta returnMeta) {
    return returnMeta.getCtType().accept(new ReturnCtTypeVisitor(queryMeta, returnMeta), false);
  }

  class ReturnCtTypeVisitor
      extends SimpleCtTypeVisitor<ResultParameterMeta, Boolean, RuntimeException> {

    final AutoFunctionQueryMeta queryMeta;

    final QueryReturnMeta returnMeta;

    ReturnCtTypeVisitor(AutoFunctionQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
    }

    @Override
    protected ResultParameterMeta defaultAction(CtType ctType, Boolean p) throws RuntimeException {
      throw new AptException(Message.DOMA4063, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public ResultParameterMeta visitBasicCtType(BasicCtType ctType, Boolean optional)
        throws RuntimeException {
      if (Boolean.TRUE == optional) {
        return new OptionalBasicSingleResultParameterMeta(ctType);
      }
      return new BasicSingleResultParameterMeta(ctType);
    }

    @Override
    public ResultParameterMeta visitDomainCtType(DomainCtType ctType, Boolean optional)
        throws RuntimeException {
      if (Boolean.TRUE == optional) {
        return new OptionalDomainSingleResultParameterMeta(ctType);
      }
      return new DomainSingleResultParameterMeta(ctType);
    }

    @Override
    public ResultParameterMeta visitIterableCtType(IterableCtType ctType, Boolean p)
        throws RuntimeException {
      if (!ctType.isList()) {
        defaultAction(ctType, p);
      }
      return ctType
          .getElementCtType()
          .accept(new IterableElementCtTypeVisitor(queryMeta, returnMeta), false);
    }

    @Override
    public ResultParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public ResultParameterMeta visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalIntSingleResultParameterMeta();
    }

    @Override
    public ResultParameterMeta visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalLongSingleResultParameterMeta();
    }

    @Override
    public ResultParameterMeta visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalDoubleSingleResultParameterMeta();
    }
  }

  class IterableElementCtTypeVisitor
      extends SimpleCtTypeVisitor<ResultParameterMeta, Boolean, RuntimeException> {

    final AutoFunctionQueryMeta queryMeta;

    final QueryReturnMeta returnMeta;

    IterableElementCtTypeVisitor(AutoFunctionQueryMeta queryMeta, QueryReturnMeta returnMeta) {
      this.queryMeta = queryMeta;
      this.returnMeta = returnMeta;
    }

    @Override
    protected ResultParameterMeta defaultAction(CtType ctType, Boolean p) throws RuntimeException {
      throw new AptException(Message.DOMA4065, methodElement, new Object[] {ctType.getType()});
    }

    @Override
    public ResultParameterMeta visitBasicCtType(BasicCtType ctType, Boolean optional)
        throws RuntimeException {
      if (Boolean.TRUE == optional) {
        return new OptionalBasicResultListParameterMeta(ctType);
      }
      return new BasicResultListParameterMeta(ctType);
    }

    @Override
    public ResultParameterMeta visitDomainCtType(DomainCtType ctType, Boolean optional)
        throws RuntimeException {
      if (Boolean.TRUE == optional) {
        return new OptionalDomainResultListParameterMeta(ctType);
      }
      return new DomainResultListParameterMeta(ctType);
    }

    @Override
    public ResultParameterMeta visitEntityCtType(EntityCtType ctType, Boolean p)
        throws RuntimeException {
      if (ctType.isAbstract()) {
        throw new AptException(Message.DOMA4156, methodElement, new Object[] {ctType.getType()});
      }
      return new EntityResultListParameterMeta(ctType, queryMeta.getEnsureResultMapping());
    }

    @Override
    public ResultParameterMeta visitMapCtType(MapCtType ctType, Boolean p) throws RuntimeException {
      return new MapResultListParameterMeta(ctType);
    }

    @Override
    public ResultParameterMeta visitOptionalCtType(OptionalCtType ctType, Boolean p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, true);
    }

    @Override
    public ResultParameterMeta visitOptionalIntCtType(OptionalIntCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalIntResultListParameterMeta();
    }

    @Override
    public ResultParameterMeta visitOptionalLongCtType(OptionalLongCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalLongResultListParameterMeta();
    }

    @Override
    public ResultParameterMeta visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Boolean p)
        throws RuntimeException {
      return new OptionalDoubleResultListParameterMeta();
    }
  }
}
