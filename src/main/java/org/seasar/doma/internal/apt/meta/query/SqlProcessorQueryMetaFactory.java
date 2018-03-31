package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.ConfigCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.PreparedSqlCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class SqlProcessorQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlProcessorQueryMeta> {

  public SqlProcessorQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    var queryMeta = createSqlContentQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    doSqlFiles(queryMeta, false, false);
    return queryMeta;
  }

  private SqlProcessorQueryMeta createSqlContentQueryMeta() {
    var sqlProcessorAnnot = ctx.getAnnots().newSqlProcessorAnnot(methodElement);
    if (sqlProcessorAnnot == null) {
      return null;
    }
    var queryMeta = new SqlProcessorQueryMeta(methodElement);
    queryMeta.setSqlProcessorAnnot(sqlProcessorAnnot);
    queryMeta.setQueryKind(QueryKind.SQL_PROCESSOR);
    return queryMeta;
  }

  @Override
  protected void doParameters(SqlProcessorQueryMeta queryMeta) {
    for (VariableElement parameter : methodElement.getParameters()) {
      final var parameterMeta = createParameterMeta(parameter);
      parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }

    if (queryMeta.getBiFunctionCtType() == null) {
      var sqlProcessorAnnot = queryMeta.getSqlProcessorAnnot();
      throw new AptException(
          Message.DOMA4433, methodElement, sqlProcessorAnnot.getAnnotationMirror());
    }
  }

  @Override
  protected void doReturnType(SqlProcessorQueryMeta queryMeta) {
    final var returnMeta = createReturnMeta();
    queryMeta.setReturnMeta(returnMeta);

    var biFunctionCtType = queryMeta.getBiFunctionCtType();
    var resultCtType = biFunctionCtType.getResultCtType();
    if (resultCtType == null || !isConvertibleReturnType(returnMeta, resultCtType)) {
      throw new AptException(Message.DOMA4436, methodElement, new Object[] {returnMeta.getType()});
    }
  }

  private boolean isConvertibleReturnType(QueryReturnMeta returnMeta, AnyCtType resultCtType) {
    if (ctx.getTypes().isSameType(returnMeta.getType(), resultCtType.getType())) {
      return true;
    }
    if (returnMeta.getType().getKind() == TypeKind.VOID) {
      return ctx.getTypes().isSameType(resultCtType.getType(), Void.class);
    }
    return false;
  }

  private class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlProcessorQueryMeta queryMeta;

    private final QueryParameterMeta parameterMeta;

    public ParamCtTypeVisitor(SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getBiFunctionCtType() != null) {
        throw new AptException(Message.DOMA4434, parameterMeta.getElement());
      }
      ctType.getFirstArgCtType().accept(new ParamBiFunctionFirstArgCtTypeVisitor(queryMeta), null);
      ctType
          .getSecondArgCtType()
          .accept(new ParamBiFunctionSecondArgCtTypeVisitor(queryMeta), null);
      queryMeta.setBiFunctionCtType(ctType);
      queryMeta.setBiFunctionParameterName(parameterMeta.getName());
      return null;
    }
  }

  private class ParamBiFunctionFirstArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlProcessorQueryMeta queryMeta;

    public ParamBiFunctionFirstArgCtTypeVisitor(SqlProcessorQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4437, queryMeta.getMethodElement());
    }

    @Override
    public Void visitConfigCtType(ConfigCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }

  private class ParamBiFunctionSecondArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    private final SqlProcessorQueryMeta queryMeta;

    public ParamBiFunctionSecondArgCtTypeVisitor(SqlProcessorQueryMeta queryMeta) {
      this.queryMeta = queryMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(Message.DOMA4435, queryMeta.getMethodElement());
    }

    @Override
    public Void visitPreparedSqlCtType(PreparedSqlCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }
}
