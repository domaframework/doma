package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.internal.apt.reflection.SelectReflection;
import org.seasar.doma.internal.apt.reflection.SuppressReflection;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMetaFactory
        extends AbstractSqlFileQueryMetaFactory<SqlFileSelectQueryMeta> {

    public SqlFileSelectQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
        super(ctx, methodElement);
    }

    @Override
    public QueryMeta createQueryMeta() {
        SqlFileSelectQueryMeta queryMeta = createSqlFileSelectQueryMeta();
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta);
        doParameters(queryMeta);
        doReturnType(queryMeta);
        doThrowTypes(queryMeta);
        doSqlFiles(queryMeta, queryMeta.isExpandable(), false);
        return queryMeta;
    }

    private SqlFileSelectQueryMeta createSqlFileSelectQueryMeta() {
        SelectReflection selectReflection = ctx.getReflections().newSelectReflection(methodElement);
        if (selectReflection == null) {
            return null;
        }
        SqlFileSelectQueryMeta queryMeta = new SqlFileSelectQueryMeta(methodElement);
        queryMeta.setSelectReflection(selectReflection);
        queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
        return queryMeta;
    }

    @Override
    protected void doParameters(final SqlFileSelectQueryMeta queryMeta) {
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

        if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
            if (queryMeta.getFunctionCtType() == null) {
                throw new AptException(Message.DOMA4247, methodElement);
            }
        } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
            if (queryMeta.getCollectorCtType() == null) {
                throw new AptException(Message.DOMA4266, methodElement);
            }
        } else {
            if (queryMeta.getFunctionCtType() != null) {
                SelectReflection selectReflection = queryMeta.getSelectReflection();
                throw new AptException(Message.DOMA4248, methodElement,
                        selectReflection.getAnnotationMirror(), selectReflection.getStrategy());
            }
        }
    }

    @Override
    protected void doReturnType(final SqlFileSelectQueryMeta queryMeta) {
        final QueryReturnMeta returnMeta = createReturnMeta();
        queryMeta.setReturnMeta(returnMeta);

        if (queryMeta.getSelectStrategyType() == SelectType.STREAM) {
            FunctionCtType functionCtType = queryMeta.getFunctionCtType();
            AnyCtType returnCtType = functionCtType.getReturnCtType();
            if (returnCtType == null
                    || !ctx.getTypes().isSameType(returnMeta.getType(), returnCtType.getType())) {
                throw new AptException(Message.DOMA4246, methodElement,
                        new Object[] { returnMeta.getType(), returnCtType.getTypeName() });
            }
        } else if (queryMeta.getSelectStrategyType() == SelectType.COLLECT) {
            CollectorCtType collectorCtType = queryMeta.getCollectorCtType();
            AnyCtType returnCtType = collectorCtType.getReturnCtType();
            if (returnCtType == null
                    || !ctx.getTypes().isSameType(returnMeta.getType(), returnCtType.getType())) {
                throw new AptException(Message.DOMA4265, methodElement,
                        new Object[] { returnMeta.getType(), returnCtType.getTypeName() });
            }
        } else {
            returnMeta.getCtType().accept(new ReturnCtTypeVisitor(queryMeta, returnMeta), null);
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ParamCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryParameterMeta parameterMeta;

        public ParamCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryParameterMeta parameterMeta) {
            this.queryMeta = queryMeta;
            this.parameterMeta = parameterMeta;
        }

        @Override
        public Void visitFunctionCtType(FunctionCtType ctType, Void p) throws RuntimeException {
            if (queryMeta.getFunctionCtType() != null) {
                throw new AptException(Message.DOMA4249, parameterMeta.getElement());
            }
            ctType.getTargetCtType()
                    .accept(new ParamFunctionTargetCtTypeVisitor(queryMeta, parameterMeta), null);
            queryMeta.setFunctionCtType(ctType);
            queryMeta.setFunctionParameterName(parameterMeta.getName());
            return null;
        }

        @Override
        public Void visitCollectorCtType(CollectorCtType ctType, Void p) throws RuntimeException {
            if (queryMeta.getCollectorCtType() != null) {
                throw new AptException(Message.DOMA4264, parameterMeta.getElement());
            }
            ctType.getTargetCtType()
                    .accept(new ParamCollectorTargetCtTypeVisitor(queryMeta, parameterMeta), null);
            queryMeta.setCollectorCtType(ctType);
            queryMeta.setCollectorParameterName(parameterMeta.getName());
            return null;
        }

        @Override
        public Void visitSelectOptionsCtType(SelectOptionsCtType ctType, Void p)
                throws RuntimeException {
            if (queryMeta.getSelectOptionsCtType() != null) {
                throw new AptException(Message.DOMA4053, parameterMeta.getElement());
            }
            queryMeta.setSelectOptionsCtType(ctType);
            queryMeta.setSelectOptionsParameterName(parameterMeta.getName());
            return null;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ParamFunctionTargetCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryParameterMeta parameterMeta;

        public ParamFunctionTargetCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryParameterMeta parameterMeta) {
            this.queryMeta = queryMeta;
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4244, queryMeta.getMethodElement());
        }

        @Override
        public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(), null);
        }

        protected class StreamElementCtTypeVisitor
                extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

            @Override
            protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
                throw new AptException(Message.DOMA4245, queryMeta.getMethodElement());
            }

            @Override
            public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
                return null;
            }

            @Override
            public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
                return null;
            }

            @Override
            public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
                return null;
            }

            @Override
            public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
                if (ctType.isAbstract()) {
                    throw new AptException(Message.DOMA4250, parameterMeta.getElement(),
                            new Object[] { ctType.getTypeName() });
                }
                queryMeta.setEntityCtType(ctType);
                return null;
            }

            @Override
            public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
                Boolean valid = ctType.getElementCtType()
                        .accept(new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

                            @Override
                            protected Boolean defaultAction(CtType ctType, Void p)
                                    throws RuntimeException {
                                return false;
                            }

                            @Override
                            public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                                    throws RuntimeException {
                                return true;
                            }

                            @Override
                            public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                                    throws RuntimeException {
                                return true;
                            }

                        }, null);
                if (Boolean.FALSE == valid) {
                    defaultAction(ctType, null);
                }
                return null;
            }

            @Override
            public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                    throws RuntimeException {
                return null;
            }

            @Override
            public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                    throws RuntimeException {
                return null;
            }

            @Override
            public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                    throws RuntimeException {
                return null;
            }

        }
    }

    private class ParamCollectorTargetCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryParameterMeta parameterMeta;

        public ParamCollectorTargetCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryParameterMeta parameterMeta) {
            this.queryMeta = queryMeta;
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4262, queryMeta.getMethodElement());
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4263, parameterMeta.getElement(),
                        new Object[] { ctType.getTypeName() });
            }
            queryMeta.setEntityCtType(ctType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            Boolean valid = ctType.getElementCtType()
                    .accept(new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

                        @Override
                        protected Boolean defaultAction(CtType ctType, Void p)
                                throws RuntimeException {
                            return false;
                        }

                        @Override
                        public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                                throws RuntimeException {
                            return true;
                        }

                        @Override
                        public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                                throws RuntimeException {
                            return true;
                        }

                    }, null);
            if (Boolean.FALSE == valid) {
                defaultAction(ctType, null);
            }
            return null;
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryReturnMeta returnMeta;

        private SuppressReflection suppressReflection;

        public ReturnCtTypeVisitor(SqlFileSelectQueryMeta queryMeta, QueryReturnMeta returnMeta) {
            this.queryMeta = queryMeta;
            this.returnMeta = returnMeta;
            this.suppressReflection = ctx.getReflections()
                    .newSuppressReflection(queryMeta.getMethodElement());
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4008, returnMeta.getMethodElement(),
                    new Object[] { returnMeta.getType() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4154, returnMeta.getMethodElement(),
                        new Object[] { ctType.getQualifiedName() });
            }
            queryMeta.setEntityCtType(ctType);
            return null;
        }

        @Override
        public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitIterableCtType(IterableCtType ctType, Void p) throws RuntimeException {
            if (!ctType.isList()) {
                defaultAction(ctType, p);
            }
            ctType.getElementCtType()
                    .accept(new ReturnListElementCtTypeVisitor(queryMeta, returnMeta), p);
            return null;
        }

        @Override
        public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
            if (!isSuppressed(Message.DOMA4274)) {
                ctx.getNotifier().send(Kind.WARNING, Message.DOMA4274,
                        returnMeta.getMethodElement(), new Object[] {});
            }
            queryMeta.setResultStream(true);
            ctType.getElementCtType()
                    .accept(new ReturnStreamElementCtTypeVisitor(queryMeta, returnMeta), p);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            ctType.getElementCtType()
                    .accept(new ReturnOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
            return null;
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        private boolean isSuppressed(Message message) {
            if (suppressReflection != null) {
                return suppressReflection.isSuppressed(message);
            }
            return false;
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnListElementCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryReturnMeta returnMeta;

        public ReturnListElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryReturnMeta returnMeta) {
            this.queryMeta = queryMeta;
            this.returnMeta = returnMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4007, returnMeta.getMethodElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4155, returnMeta.getMethodElement(),
                        new Object[] { ctType.getType() });
            }
            queryMeta.setEntityCtType(ctType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            ctType.getElementCtType()
                    .accept(new ReturnListOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
            return null;
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnStreamElementCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryReturnMeta returnMeta;

        public ReturnStreamElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryReturnMeta returnMeta) {
            this.queryMeta = queryMeta;
            this.returnMeta = returnMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4271, returnMeta.getMethodElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4272, returnMeta.getMethodElement(),
                        new Object[] { ctType.getType() });
            }
            queryMeta.setEntityCtType(ctType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            ctType.getElementCtType()
                    .accept(new ReturnStreamOptionalElementCtTypeVisitor(queryMeta, returnMeta), p);
            return null;
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return null;
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnOptionalElementCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private SqlFileSelectQueryMeta queryMeta;

        private QueryReturnMeta returnMeta;

        public ReturnOptionalElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryReturnMeta returnMeta) {
            this.queryMeta = queryMeta;
            this.returnMeta = returnMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4235, returnMeta.getMethodElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitMapCtType(MapCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4234, returnMeta.getMethodElement(),
                        new Object[] { ctType.getType() });
            }
            queryMeta.setEntityCtType(ctType);
            return null;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnListOptionalElementCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private QueryReturnMeta returnMeta;

        public ReturnListOptionalElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryReturnMeta returnMeta) {
            this.returnMeta = returnMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4267, returnMeta.getMethodElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    private class ReturnStreamOptionalElementCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private QueryReturnMeta returnMeta;

        protected ReturnStreamOptionalElementCtTypeVisitor(SqlFileSelectQueryMeta queryMeta,
                QueryReturnMeta returnMeta) {
            this.returnMeta = returnMeta;
        }

        @Override
        protected Void defaultAction(CtType type, Void p) throws RuntimeException {
            throw new AptException(Message.DOMA4267, returnMeta.getMethodElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public Void visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p) throws RuntimeException {
            return null;
        }
    }

}
