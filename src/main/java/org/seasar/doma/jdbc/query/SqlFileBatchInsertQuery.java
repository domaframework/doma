package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ListIterator;

import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;

public class SqlFileBatchInsertQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
        implements BatchInsertQuery {

    protected EntityHandler entityHandler;

    public SqlFileBatchInsertQuery(Class<ELEMENT> elementClass) {
        super(elementClass, SqlKind.BATCH_INSERT);
    }

    @Override
    public void prepare() {
        super.prepare();
        int size = elements.size();
        if (size == 0) {
            return;
        }
        executable = true;
        sqlExecutionSkipCause = null;
        currentEntity = elements.get(0);
        preInsert();
        prepareSqlFile();
        prepareOptions();
        prepareSql();
        elements.set(0, currentEntity);
        for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext();) {
            currentEntity = it.next();
            preInsert();
            prepareSql();
            it.set(currentEntity);
        }
        assertEquals(size, sqls.size());
    }

    protected void preInsert() {
        if (entityHandler != null) {
            entityHandler.preInsert();
        }
    }

    @Override
    public void generateId(Statement statement, int index) {
    }

    @Override
    public void complete() {
        if (entityHandler != null) {
            for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext();) {
                currentEntity = it.next();
                entityHandler.postInsert();
                it.set(currentEntity);
            }
        }
    }

    @Override
    public void setEntityDesc(EntityDesc<ELEMENT> entityDesc) {
        entityHandler = new EntityHandler(entityDesc);
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    protected class EntityHandler {

        protected final EntityDesc<ELEMENT> entityDesc;

        protected EntityHandler(EntityDesc<ELEMENT> entityDesc) {
            assertNotNull(entityDesc);
            this.entityDesc = entityDesc;
        }

        protected void preInsert() {
            SqlFileBatchPreInsertContext<ELEMENT> context = new SqlFileBatchPreInsertContext<>(
                    entityDesc, method, config);
            entityDesc.preInsert(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }

        protected void postInsert() {
            SqlFileBatchPostInsertContext<ELEMENT> context = new SqlFileBatchPostInsertContext<>(
                    entityDesc, method, config);
            entityDesc.postInsert(currentEntity, context);
            if (context.getNewEntity() != null) {
                currentEntity = context.getNewEntity();
            }
        }
    }

    protected static class SqlFileBatchPreInsertContext<E> extends AbstractPreInsertContext<E> {

        public SqlFileBatchPreInsertContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
        }
    }

    protected static class SqlFileBatchPostInsertContext<E> extends AbstractPostInsertContext<E> {

        public SqlFileBatchPostInsertContext(EntityDesc<E> entityDesc, Method method,
                Config config) {
            super(entityDesc, method, config);
        }
    }
}
