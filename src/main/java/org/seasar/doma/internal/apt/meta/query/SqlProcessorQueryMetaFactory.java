package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.annot.SqlProcessorAnnot;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.ConfigCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.PreparedSqlCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

public class SqlProcessorQueryMetaFactory
    extends AbstractSqlFileQueryMetaFactory<SqlProcessorQueryMeta> {

  public SqlProcessorQueryMetaFactory(ProcessingEnvironment env) {
    super(env);
  }

  @Override
  public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
    assertNotNull(method, daoMeta);
    SqlProcessorQueryMeta queryMeta = createSqlContentQueryMeta(method, daoMeta);
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta, method, daoMeta);
    doParameters(queryMeta, method, daoMeta);
    doReturnType(queryMeta, method, daoMeta);
    doThrowTypes(queryMeta, method, daoMeta);
    doSqlFiles(queryMeta, method, daoMeta, false, false);
    return queryMeta;
  }

  protected SqlProcessorQueryMeta createSqlContentQueryMeta(
      ExecutableElement method, DaoMeta daoMeta) {
    SqlProcessorAnnot sqlProcessorAnnot = SqlProcessorAnnot.newInstance(method, env);
    if (sqlProcessorAnnot == null) {
      return null;
    }
    SqlProcessorQueryMeta queryMeta = new SqlProcessorQueryMeta(method, daoMeta.getDaoElement());
    queryMeta.setSqlProcessorAnnot(sqlProcessorAnnot);
    queryMeta.setQueryKind(QueryKind.SQL_PROCESSOR);
    return queryMeta;
  }

  @Override
  protected void doParameters(
      SqlProcessorQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    for (VariableElement parameter : method.getParameters()) {
      final QueryParameterMeta parameterMeta = createParameterMeta(parameter, queryMeta);
      parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.addParameterMeta(parameterMeta);
      if (parameterMeta.isBindable()) {
        queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
      }
    }

    if (queryMeta.getBiFunctionCtType() == null) {
      SqlProcessorAnnot sqlProcessorAnnot = queryMeta.getSqlProcessorAnnot();
      throw new AptException(
          Message.DOMA4433,
          env,
          method,
          sqlProcessorAnnot.getAnnotationMirror(),
          new Object[] {daoMeta.getDaoElement().getQualifiedName(), method.getSimpleName()});
    }
  }

  @Override
  protected void doReturnType(
      SqlProcessorQueryMeta queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    final QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
    queryMeta.setReturnMeta(returnMeta);

    BiFunctionCtType biFunctionCtType = queryMeta.getBiFunctionCtType();
    AnyCtType resultCtType = biFunctionCtType.getResultCtType();
    if (resultCtType == null || !isConvertibleReturnType(returnMeta, resultCtType)) {
      throw new AptException(
          Message.DOMA4436,
          env,
          method,
          new Object[] {
            returnMeta.getType(),
            resultCtType.getBoxedTypeName(),
            daoMeta.getDaoElement().getQualifiedName(),
            method.getSimpleName()
          });
    }
  }

  protected boolean isConvertibleReturnType(QueryReturnMeta returnMeta, AnyCtType resultCtType) {
    if (env.getTypeUtils().isSameType(returnMeta.getType(), resultCtType.getTypeMirror())) {
      return true;
    }
    if (returnMeta.getType().getKind() == TypeKind.VOID) {
      return TypeMirrorUtil.isSameType(resultCtType.getTypeMirror(), Void.class, env);
    }
    return false;
  }

  protected class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected SqlProcessorQueryMeta queryMeta;

    protected QueryParameterMeta parameterMeta;

    protected ParamCtTypeVisitor(
        SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p) throws RuntimeException {
      if (queryMeta.getBiFunctionCtType() != null) {
        throw new AptException(
            Message.DOMA4434,
            env,
            parameterMeta.getElement(),
            new Object[] {
              parameterMeta.getDaoElement().getQualifiedName(),
              parameterMeta.getMethodElement().getSimpleName()
            });
      }
      ctType
          .getFirstArgCtType()
          .accept(new ParamBiFunctionFirstArgCtTypeVisitor(queryMeta, parameterMeta), null);
      ctType
          .getSecondArgCtType()
          .accept(new ParamBiFunctionSecondArgCtTypeVisitor(queryMeta, parameterMeta), null);
      queryMeta.setBiFunctionCtType(ctType);
      queryMeta.setBiFunctionParameterName(parameterMeta.getName());
      return null;
    }
  }

  protected class ParamBiFunctionFirstArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected SqlProcessorQueryMeta queryMeta;

    protected QueryParameterMeta parameterMeta;

    protected ParamBiFunctionFirstArgCtTypeVisitor(
        SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4437,
          env,
          queryMeta.getMethodElement(),
          new Object[] {
            parameterMeta.getDaoElement().getQualifiedName(),
            parameterMeta.getMethodElement().getSimpleName()
          });
    }

    @Override
    public Void visitConfigCtType(ConfigCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }

  protected class ParamBiFunctionSecondArgCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected SqlProcessorQueryMeta queryMeta;

    protected QueryParameterMeta parameterMeta;

    protected ParamBiFunctionSecondArgCtTypeVisitor(
        SqlProcessorQueryMeta queryMeta, QueryParameterMeta parameterMeta) {
      this.queryMeta = queryMeta;
      this.parameterMeta = parameterMeta;
    }

    @Override
    protected Void defaultAction(CtType type, Void p) throws RuntimeException {
      throw new AptException(
          Message.DOMA4435,
          env,
          queryMeta.getMethodElement(),
          new Object[] {
            parameterMeta.getDaoElement().getQualifiedName(),
            parameterMeta.getMethodElement().getSimpleName()
          });
    }

    @Override
    public Void visitPreparedSqlCtType(PreparedSqlCtType ctType, Void p) throws RuntimeException {
      return null;
    }
  }
}
