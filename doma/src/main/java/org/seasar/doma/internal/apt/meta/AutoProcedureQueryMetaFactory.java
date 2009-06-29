package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Procedure;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class AutoProcedureQueryMetaFactory extends AutoModuleQueryMetaFactory {

    public AutoProcedureQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoProcedureQueryMeta queryMeta = new AutoProcedureQueryMeta();
        Procedure procedure = method.getAnnotation(Procedure.class);
        if (procedure == null) {
            return null;
        }
        queryMeta.setQueryTimeout(procedure.queryTimeout());
        queryMeta.setQueryKind(QueryKind.AUTO_PROCEDURE);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        doProcedureName(procedure, queryMeta, method, daoMeta);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected void doProcedureName(Procedure procedure,
            AutoProcedureQueryMeta queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        StringBuilder buf = new StringBuilder();
        if (!procedure.catalog().isEmpty()) {
            buf.append(procedure.catalog());
            buf.append(".");
        }
        if (!procedure.schema().isEmpty()) {
            buf.append(procedure.schema());
            buf.append(".");
        }
        if (!procedure.name().isEmpty()) {
            buf.append(procedure.name());
        } else {
            buf.append(queryMeta.getName());
        }
        queryMeta.setProcedureName(buf.toString());
    }

    protected void doReturnType(AutoProcedureQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        if (!isPrimitiveVoid(returnType)) {
            throw new AptException(MessageCode.DOMA4064, env, method);
        }
        String typeName = Models.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env);
        queryMeta.setReturnTypeName(typeName);
    }
}
