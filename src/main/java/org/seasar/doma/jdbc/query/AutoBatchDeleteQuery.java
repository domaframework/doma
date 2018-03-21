package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.ListIterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.Property;

public class AutoBatchDeleteQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
        implements BatchDeleteQuery {

    protected boolean versionIgnored;

    protected boolean optimisticLockExceptionSuppressed;

    public AutoBatchDeleteQuery(EntityDesc<ENTITY> entityDesc) {
        super(entityDesc);
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(method, entities, sqls);
        int size = entities.size();
        if (size == 0) {
            return;
        }
        executable = true;
        executionSkipCause = null;
        currentEntity = entities.get(0);
        preDelete();
        prepareIdAndVersionPropertyDescs();
        validateIdExistent();
        prepareOptions();
        prepareOptimisticLock();
        prepareSql();
        entities.set(0, currentEntity);
        for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext();) {
            currentEntity = it.next();
            preDelete();
            prepareSql();
            it.set(currentEntity);
        }
        assertEquals(size, sqls.size());
    }

    protected void preDelete() {
        AutoBatchPreDeleteContext<ENTITY> context = new AutoBatchPreDeleteContext<>(
                entityDesc, method, config);
        entityDesc.preDelete(currentEntity, context);
        if (context.getNewEntity() != null) {
            currentEntity = context.getNewEntity();
        }
    }

    protected void prepareOptimisticLock() {
        if (versionPropertyDesc != null && !versionIgnored) {
            if (!optimisticLockExceptionSuppressed) {
                optimisticLockCheckRequired = true;
            }
        }
    }

    protected void prepareSql() {
        Naming naming = config.getNaming();
        Dialect dialect = config.getDialect();
        PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_DELETE,
                sqlLogType);
        builder.appendSql("delete from ");
        builder.appendSql(entityDesc.getQualifiedTableName(naming::apply, dialect::applyQuote));
        if (idPropertyDescs.size() > 0) {
            builder.appendSql(" where ");
            for (EntityPropertyDesc<ENTITY, ?> propertyDesc : idPropertyDescs) {
                Property<ENTITY, ?> property = propertyDesc.createProperty();
                property.load(currentEntity);
                builder.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
                builder.appendSql(" = ");
                builder.appendParameter(property.asInParameter());
                builder.appendSql(" and ");
            }
            builder.cutBackSql(5);
        }
        if (versionPropertyDesc != null && !versionIgnored) {
            if (idPropertyDescs.size() == 0) {
                builder.appendSql(" where ");
            } else {
                builder.appendSql(" and ");
            }
            Property<ENTITY, ?> property = versionPropertyDesc.createProperty();
            property.load(currentEntity);
            builder.appendSql(
                    versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
            builder.appendSql(" = ");
            builder.appendParameter(property.asInParameter());
        }
        PreparedSql sql = builder.build(this::comment);
        sqls.add(sql);
    }

    @Override
    public void complete() {
        for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext();) {
            currentEntity = it.next();
            postDelete();
            it.set(currentEntity);
        }
    }

    protected void postDelete() {
        AutoBatchPostDeleteContext<ENTITY> context = new AutoBatchPostDeleteContext<>(
                entityDesc, method, config);
        entityDesc.postDelete(currentEntity, context);
        if (context.getNewEntity() != null) {
            currentEntity = context.getNewEntity();
        }
    }

    public void setVersionIgnored(boolean versionIgnored) {
        this.versionIgnored = versionIgnored;
    }

    public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
        this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    }

    protected static class AutoBatchPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

        public AutoBatchPreDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
            super(entityDesc, method, config);
        }
    }

    protected static class AutoBatchPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

        public AutoBatchPostDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
            super(entityDesc, method, config);
        }
    }
}
