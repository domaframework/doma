package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;

public class AutoUpdateQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements UpdateQuery {

    protected boolean nullExcluded;

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    protected boolean unchangedPropertyIncluded;

    protected UpdateQueryHelper<ENTITY> helper;

    public AutoUpdateQuery(EntityDesc<ENTITY> entityDesc) {
        super(entityDesc);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, entityDesc, entity);
        setupHelper();
        preUpdate();
        prepareIdAndVersionPropertyDescs();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareTargetPropertyDescs();
        prepareSql();
        assertNotNull(sql);
    }

    protected void setupHelper() {
        helper = new UpdateQueryHelper<>(config, entityDesc, includedPropertyNames,
                excludedPropertyNames, nullExcluded, versionIgnored,
                optimisticLockExceptionSuppressed, unchangedPropertyIncluded);
    }

    protected void preUpdate() {
        List<EntityPropertyDesc<ENTITY, ?>> targetPropertyDescs = helper
                .getTargetPropertyDescs(entity);
        AutoPreUpdateContext<ENTITY> context = new AutoPreUpdateContext<>(entityDesc, method,
                config, targetPropertyDescs);
        entityDesc.preUpdate(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
    }

    protected void prepareOptimisticLock() {
        if (!versionIgnored && versionPropertyDesc != null) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareTargetPropertyDescs() {
        targetPropertyDescs = helper.getTargetPropertyDescs(entity);
        if (!targetPropertyDescs.isEmpty()) {
            executable = true;
            sqlExecutionSkipCause = null;
        }
    }

    protected void prepareSql() {
        Naming naming = config.getNaming();
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.UPDATE, sqlLogType);
        builder.appendSql("update ");
        builder.appendSql(entityDesc.getQualifiedTableName(naming::apply, dialect::applyQuote));
        builder.appendSql(" set ");
        helper.populateValues(entity, targetPropertyDescs, versionPropertyDesc, builder);
        if (idPropertyDescs.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyDesc<ENTITY, ?> propertyDesc : idPropertyDescs) {
                Property<ENTITY, ?> property = propertyDesc.createProperty();
                property.load(entity);
                builder.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
                builder.appendSql(" = ");
                builder.appendParameter(property.asInParameter());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (!versionIgnored && versionPropertyDesc != null) {
            if (idPropertyDescs.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            Property<ENTITY, ?> property = versionPropertyDesc.createProperty();
            property.load(entity);
            builder.appendSql(
                    versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property.asInParameter());
        }
        sql = builder.build(this::comment);
    }

    @Override
    public void incrementVersion() {
        if (!versionIgnored && versionPropertyDesc != null) {
            entity = versionPropertyDesc.increment(entityDesc, entity);
        }
    }

    @Override
    public void complete() {
        postUpdate();
    }

    protected void postUpdate() {
        List<EntityPropertyDesc<ENTITY, ?>> targetPropertyDescs = helper
                .getTargetPropertyDescs(entity);
        if (!versionIgnored && versionPropertyDesc != null) {
            targetPropertyDescs.add(versionPropertyDesc);
        }
        AutoPostUpdateContext<ENTITY> context = new AutoPostUpdateContext<>(entityDesc,
                method, config, targetPropertyDescs);
        entityDesc.postUpdate(entity, context);
        if (context.getNewEntity() != null) {
            entity = context.getNewEntity();
        }
        entityDesc.saveCurrentStates(entity);
    }

    public void setNullExcluded(boolean nullExcluded) {
        this.nullExcluded = nullExcluded;
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored |= versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
        this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    }

    protected static class AutoPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

        protected final Set<String> changedPropertyNames;

        public AutoPreUpdateContext(EntityDesc<E> entityDesc, Method method, Config config,
                List<EntityPropertyDesc<E, ?>> targetPropertyDescs) {
            super(entityDesc, method, config);
            assertNotNull(targetPropertyDescs);
            changedPropertyNames = new HashSet<>(targetPropertyDescs.size());
            for (EntityPropertyDesc<E, ?> propertyDesc : targetPropertyDescs) {
                changedPropertyNames.add(propertyDesc.getName());
            }
        }

        @Override
        public boolean isEntityChanged() {
            return !changedPropertyNames.isEmpty();
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return changedPropertyNames.contains(propertyName);
        }
    }

    protected static class AutoPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

        protected final Set<String> changedPropertyNames;

        public AutoPostUpdateContext(EntityDesc<E> entityDesc, Method method, Config config,
                List<EntityPropertyDesc<E, ?>> targetPropertyDescs) {
            super(entityDesc, method, config);
            assertNotNull(targetPropertyDescs);
            changedPropertyNames = new HashSet<>(targetPropertyDescs.size());
            for (EntityPropertyDesc<E, ?> propertyDesc : targetPropertyDescs) {
                changedPropertyNames.add(propertyDesc.getName());
            }
        }

        @Override
        public boolean isPropertyChanged(String propertyName) {
            validatePropertyDefined(propertyName);
            return changedPropertyNames.contains(propertyName);
        }
    }

}
