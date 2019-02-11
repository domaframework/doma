package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
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
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.parameter.*;
import org.seasar.doma.message.Message;

public class AutoFunctionQueryMetaFactory
    extends AutoModuleQueryMetaFactory<AutoFunctionQueryMeta> {

  public AutoFunctionQueryMetaFactory(Context ctx) {
    super(ctx);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    FunctionAnnot functionAnnot = ctx.getAnnotations().newFunctionAnnot(method);
    if (functionAnnot == null) {
      return null;
    }
    AutoFunctionQueryMeta queryMeta = new AutoFunctionQueryMeta(method, daoMeta.getTypeElement());
    queryMeta.setFunctionAnnot(functionAnnot);
    queryMeta.setQueryKind(QueryKind.AUTO_FUNCTION);
    doTypeParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    return queryMeta;
  }

  @Override
  protected void doReturnType(
      AutoFunctionQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
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
    protected ResultParameterMeta defaultAction(CtType type, Boolean p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4063, returnMeta.getMethodElement(), new Object[] {returnMeta.getType()});
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
      throw new AptException(
          Message.DOMA4065, returnMeta.getMethodElement(), new Object[] {ctType.getType()});
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
        throw new AptException(
            Message.DOMA4156, returnMeta.getMethodElement(), new Object[] {ctType.getType()});
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
