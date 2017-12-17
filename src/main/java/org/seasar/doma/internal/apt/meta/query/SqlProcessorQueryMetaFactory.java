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
import org.seasar.doma.internal.apt.reflection.SqlProcessorReflection;
import org.seasar.doma.message.Message;

/**
 * @author nakamura
 *
 */
public class SqlProcessorQueryMetaFactory
        extends AbstractSqlFileQueryMetaFactory<SqlProcessorQueryMeta> {

    public SqlProcessorQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
        super(ctx, methodElement);
    }

    @Override
    public QueryMeta createQueryMeta() {
        SqlProcessorQueryMeta queryMeta = createSqlContentQueryMeta();
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
        SqlProcessorReflection sqlProcessorReflection = ctx.getReflections()
                .newSqlProcessorReflection(methodElement);
        if (sqlProcessorReflection == null) {
            return null;
        }
        SqlProcessorQueryMeta queryMeta = new SqlProcessorQueryMeta(methodElement);
        queryMeta.setSqlProcessorReflection(sqlProcessorReflection);
        queryMeta.setQueryKind(QueryKind.SQL_PROCESSOR);
        return queryMeta;
    }

    @Override
    protected void doParameters(SqlProcessorQueryMeta queryMeta) {
        for (VariableElement parameter : methodElement.getParameters()) {
            final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            parameterMeta.getCtType().accept(new ParamCtTypeVisitor(queryMeta, parameterMeta),
                    null);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
            }
        }

        if (queryMeta.getBiFunctionCtType() == null) {
            SqlProcessorReflection sqlProcessorReflection = queryMeta.getSqlProcessorReflection();
            throw new AptException(Message.DOMA4433, methodElement,
                    sqlProcessorReflection.getAnnotationMirror());
        }
    }

    @Override
    protected void doReturnType(SqlProcessorQueryMeta queryMeta) {
        final QueryReturnMeta returnMeta = createReturnMeta();
        queryMeta.setReturnMeta(returnMeta);

        BiFunctionCtType biFunctionCtType = queryMeta.getBiFunctionCtType();
        AnyCtType resultCtType = biFunctionCtType.getResultCtType();
        if (resultCtType == null || !isConvertibleReturnType(returnMeta, resultCtType)) {
            throw new AptException(Message.DOMA4436, methodElement,
                    new Object[] { returnMeta.getType(), resultCtType.getTypeName() });
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

    /**
     * 
     * @author nakamura
     *
     */
    private class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlProcessorQueryMeta queryMeta;

        private QueryParameterMeta parameterMeta;

        public ParamCtTypeVisitor(SqlProcessorQueryMeta queryMeta,
                QueryParameterMeta parameterMeta) {
            this.queryMeta = queryMeta;
            this.parameterMeta = parameterMeta;
        }

        @Override
        public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p) throws RuntimeException {
            if (queryMeta.getBiFunctionCtType() != null) {
                throw new AptException(Message.DOMA4434, parameterMeta.getElement());
            }
            ctType.getFirstArgCtType().accept(new ParamBiFunctionFirstArgCtTypeVisitor(queryMeta),
                    null);
            ctType.getSecondArgCtType().accept(new ParamBiFunctionSecondArgCtTypeVisitor(queryMeta),
                    null);
            queryMeta.setBiFunctionCtType(ctType);
            queryMeta.setBiFunctionParameterName(parameterMeta.getName());
            return null;
        }

    }

    /**
     * 
     * @author nakamura
     *
     */
    private class ParamBiFunctionFirstArgCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlProcessorQueryMeta queryMeta;

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

    /**
     * 
     * @author nakamura
     *
     */
    private class ParamBiFunctionSecondArgCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlProcessorQueryMeta queryMeta;

        public ParamBiFunctionSecondArgCtTypeVisitor(SqlProcessorQueryMeta queryMeta) {
            this.queryMeta = queryMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4435, queryMeta.getMethodElement());
        }

        @Override
        public Void visitPreparedSqlCtType(PreparedSqlCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }
    }

}
