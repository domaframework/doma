package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class AutoModifyQueryMetaFactory extends AbstractQueryMetaFactory {

    public AutoModifyQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoModifyQueryMeta queryMeta = createAutoModifyQueryMeta(method, daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected AutoModifyQueryMeta createAutoModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        AutoModifyQueryMeta queryMeta = new AutoModifyQueryMeta();
        Insert insert = method.getAnnotation(Insert.class);
        if (insert != null && !insert.sqlFile()) {
            queryMeta.setQueryTimeout(insert.queryTimeout());
            queryMeta.setNullExcluded(insert.excludesNull());
            queryMeta.setQueryKind(QueryKind.AUTO_INSERT);
        }
        Update update = method.getAnnotation(Update.class);
        if (update != null && !update.sqlFile()) {
            queryMeta.setQueryTimeout(update.queryTimeout());
            queryMeta.setNullExcluded(update.excludesNull());
            queryMeta.setVersionIncluded(update.includesVersion());
            queryMeta.setOptimisticLockExceptionSuppressed(update
                    .suppressesOptimisticLockException());
            queryMeta.setQueryKind(QueryKind.AUTO_UPDATE);
        }
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null && !delete.sqlFile()) {
            queryMeta.setQueryTimeout(delete.queryTimeout());
            queryMeta.setVersionIgnored(delete.ignoresVersion());
            queryMeta.setOptimisticLockExceptionSuppressed(delete
                    .suppressesOptimisticLockException());
            queryMeta.setQueryKind(QueryKind.AUTO_DELETE);
        }
        if (queryMeta.getQueryKind() == null) {
            return null;
        }
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    protected void doReturnType(AutoModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        if (!isPrimitiveInt(returnType)) {
            throw new AptException(MessageCode.DOMA4001, env, method);
        }
        queryMeta.setReturnTypeName(Models.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env));
    }

    protected void doParameters(AutoModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> params = method.getParameters();
        int size = params.size();
        if (size != 1) {
            throw new AptException(MessageCode.DOMA4002, env, method);
        }
        VariableElement entity = params.get(0);
        TypeMirror entityType = Models.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), entity.asType());
        if (!isEntity(entityType, daoMeta)) {
            throw new AptException(MessageCode.DOMA4003, env, entity);
        }
        queryMeta.setEntityName(Models.getParameterName(entity));
        queryMeta.setEntityTypeName(Models.getTypeName(entityType, daoMeta
                .getTypeParameterMap(), env));
    }

}
