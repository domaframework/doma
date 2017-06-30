/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ResultSetReflection;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected AutoModuleQueryMetaFactory(Context ctx) {
        super(ctx);
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);

            CallableSqlParameterMeta callableSqlParameterMeta = createParameterMeta(parameterMeta);
            queryMeta.addCallableSqlParameterMeta(callableSqlParameterMeta);

            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
            }
        }
    }

    protected CallableSqlParameterMeta createParameterMeta(
            final QueryParameterMeta parameterMeta) {
        ResultSetReflection resultSetReflection = ctx.getReflections()
                .newResultSetReflection(parameterMeta.getElement());
        if (resultSetReflection != null) {
            return createResultSetParameterMeta(parameterMeta, resultSetReflection);
        }
        if (parameterMeta.isAnnotated(In.class)) {
            return createInParameterMeta(parameterMeta);
        }
        if (parameterMeta.isAnnotated(Out.class)) {
            return createOutParameterMeta(parameterMeta);
        }
        if (parameterMeta.isAnnotated(InOut.class)) {
            return createInOutParameterMeta(parameterMeta);
        }
        throw new AptException(Message.DOMA4066, parameterMeta.getElement());
    }

    protected CallableSqlParameterMeta createResultSetParameterMeta(
            final QueryParameterMeta parameterMeta,
            final ResultSetReflection resultSetReflection) {
        IterableCtType iterableCtType = parameterMeta.getCtType().accept(
                new ResultSetCtTypeVisitor(parameterMeta), null);
        return iterableCtType.getElementCtType().accept(
                new ResultSetElementCtTypeVisitor(parameterMeta,
                        resultSetReflection), false);
    }

    protected CallableSqlParameterMeta createInParameterMeta(
            final QueryParameterMeta parameterMeta) {
        return parameterMeta.getCtType().accept(
                new InCtTypeVisitor(parameterMeta), false);
    }

    protected CallableSqlParameterMeta createOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceCtType referenceCtType = parameterMeta.getCtType()
                .accept(new OutCtTypeVisitor(parameterMeta), null);
        return referenceCtType.getReferentCtType().accept(
                new OutReferentCtTypeVisitor(parameterMeta, referenceCtType),
                false);
    }

    protected CallableSqlParameterMeta createInOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceCtType referenceCtType = parameterMeta.getCtType()
                .accept(new InOutCtTypeVisitor(parameterMeta), null);
        return referenceCtType.getReferentCtType().accept(
                new InOutReferentCtTypeVisitor(parameterMeta, referenceCtType),
                false);
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class ResultSetCtTypeVisitor extends
            SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        protected ResultSetCtTypeVisitor(QueryParameterMeta parameterMeta) {
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected IterableCtType defaultAction(CtType type, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4062,
                    parameterMeta.getElement());
        }

        @Override
        public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            if (!ctType.isList()) {
                defaultAction(ctType, p);
            }
            return ctType;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class ResultSetElementCtTypeVisitor
            extends
            SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        protected final ResultSetReflection resultSetReflection;

        public ResultSetElementCtTypeVisitor(QueryParameterMeta parameterMeta,
                ResultSetReflection resultSetReflection) {
            this.parameterMeta = parameterMeta;
            this.resultSetReflection = resultSetReflection;
        }

        @Override
        protected CallableSqlParameterMeta defaultAction(CtType type, Boolean p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4186, parameterMeta.getElement(),
                    new Object[] { type.getTypeName() });
        }

        @Override
        public CallableSqlParameterMeta visitEntityCtType(EntityCtType ctType,
                Boolean p) throws RuntimeException {
            if (ctType.isAbstract()) {
                throw new AptException(Message.DOMA4157, parameterMeta.getElement(),
                        new Object[] { ctType.getTypeName() });
            }
            return new EntityListParameterMeta(parameterMeta.getName(), ctType,
                    resultSetReflection.getEnsureResultMappingValue());
        }

        @Override
        public CallableSqlParameterMeta visitMapCtType(MapCtType ctType,
                Boolean p) throws RuntimeException {
            return new MapListParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitBasicCtType(BasicCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalBasicListParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new BasicListParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitHolderCtType(HolderCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalHolderListParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new HolderListParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalCtType(
                OptionalCtType ctType, Boolean p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, true);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalIntCtType(
                OptionalIntCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalIntListParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalLongCtType(
                OptionalLongCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalLongListParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalDoubleCtType(
                OptionalDoubleCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalDoubleListParameterMeta(parameterMeta.getName());
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class InCtTypeVisitor
            extends
            SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        public InCtTypeVisitor(QueryParameterMeta parameterMeta) {
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected CallableSqlParameterMeta defaultAction(CtType ctType,
                Boolean p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4101, parameterMeta.getElement(),
                    new Object[] { ctType.getType() });
        }

        @Override
        public CallableSqlParameterMeta visitBasicCtType(BasicCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalBasicInParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new BasicInParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitHolderCtType(HolderCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalHolderInParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new HolderInParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalCtType(
                OptionalCtType ctType, Boolean p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, true);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalIntCtType(
                OptionalIntCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalIntInParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalLongCtType(
                OptionalLongCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalLongInParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalDoubleCtType(
                OptionalDoubleCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalDoubleInParameterMeta(parameterMeta.getName());
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class OutCtTypeVisitor extends
            SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        public OutCtTypeVisitor(QueryParameterMeta parameterMeta) {
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected ReferenceCtType defaultAction(CtType type, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4098,
                    parameterMeta.getElement());
        }

        @Override
        public ReferenceCtType visitReferenceCtType(ReferenceCtType ctType,
                Void p) throws RuntimeException {
            return ctType;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class OutReferentCtTypeVisitor
            extends
            SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        protected final ReferenceCtType referenceCtType;

        public OutReferentCtTypeVisitor(QueryParameterMeta parameterMeta,
                ReferenceCtType referenceCtType) {
            this.parameterMeta = parameterMeta;
            this.referenceCtType = referenceCtType;
        }

        @Override
        protected CallableSqlParameterMeta defaultAction(CtType type, Boolean p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4100, parameterMeta.getElement(),
                    new Object[] {
                            referenceCtType.getReferentCtType()
                                    .getType() });
        }

        @Override
        public CallableSqlParameterMeta visitBasicCtType(BasicCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalBasicOutParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new BasicOutParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitHolderCtType(HolderCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalHolderOutParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new HolderOutParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalCtType(
                OptionalCtType ctType, Boolean p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, true);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalIntCtType(
                OptionalIntCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalIntOutParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalLongCtType(
                OptionalLongCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalLongOutParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalDoubleCtType(
                OptionalDoubleCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalDoubleOutParameterMeta(parameterMeta.getName());
        }

    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class InOutCtTypeVisitor extends
            SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        public InOutCtTypeVisitor(QueryParameterMeta parameterMeta) {
            this.parameterMeta = parameterMeta;
        }

        @Override
        protected ReferenceCtType defaultAction(CtType type, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4111,
                    parameterMeta.getElement());
        }

        @Override
        public ReferenceCtType visitReferenceCtType(ReferenceCtType ctType,
                Void p) throws RuntimeException {
            return ctType;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class InOutReferentCtTypeVisitor
            extends
            SimpleCtTypeVisitor<CallableSqlParameterMeta, Boolean, RuntimeException> {

        protected final QueryParameterMeta parameterMeta;

        protected final ReferenceCtType referenceCtType;

        public InOutReferentCtTypeVisitor(QueryParameterMeta parameterMeta,
                ReferenceCtType referenceCtType) {
            this.parameterMeta = parameterMeta;
            this.referenceCtType = referenceCtType;
        }

        @Override
        protected CallableSqlParameterMeta defaultAction(CtType type, Boolean p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4100, parameterMeta.getElement(),
                    new Object[] {
                            referenceCtType.getReferentCtType()
                                    .getType() });
        }

        @Override
        public CallableSqlParameterMeta visitBasicCtType(BasicCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalBasicInOutParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new BasicInOutParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitHolderCtType(HolderCtType ctType,
                Boolean optional) throws RuntimeException {
            if (Boolean.TRUE == optional) {
                return new OptionalHolderInOutParameterMeta(
                        parameterMeta.getName(), ctType);
            }
            return new HolderInOutParameterMeta(parameterMeta.getName(), ctType);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalCtType(
                OptionalCtType ctType, Boolean p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, true);
        }

        @Override
        public CallableSqlParameterMeta visitOptionalIntCtType(
                OptionalIntCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalIntInOutParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalLongCtType(
                OptionalLongCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalLongInOutParameterMeta(parameterMeta.getName());
        }

        @Override
        public CallableSqlParameterMeta visitOptionalDoubleCtType(
                OptionalDoubleCtType ctType, Boolean p) throws RuntimeException {
            return new OptionalDoubleInOutParameterMeta(parameterMeta.getName());
        }

    }
}
