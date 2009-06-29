package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Function;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMetaFactory extends AutoModuleQueryMetaFactory {

    public AutoFunctionQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoFunctionQueryMeta queryMeta = new AutoFunctionQueryMeta();
        Function function = method.getAnnotation(Function.class);
        if (function == null) {
            return null;
        }
        queryMeta.setQueryTimeout(function.queryTimeout());
        queryMeta.setQueryKind(QueryKind.AUTO_FUNCTION);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        doFunctionName(function, queryMeta, method, daoMeta);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected void doFunctionName(Function function,
            AutoFunctionQueryMeta queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        StringBuilder buf = new StringBuilder();
        if (!function.catalog().isEmpty()) {
            buf.append(function.catalog());
            buf.append(".");
        }
        if (!function.schema().isEmpty()) {
            buf.append(function.schema());
            buf.append(".");
        }
        if (!function.name().isEmpty()) {
            buf.append(function.name());
        } else {
            buf.append(queryMeta.getName());
        }
        queryMeta.setFunctionName(buf.toString());
    }

    protected void doReturnType(AutoFunctionQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        String typeName = Models.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env);
        queryMeta.setReturnTypeName(typeName);
        ResultParameterMeta resultParameterMeta = createResultParameterMeta(queryMeta, returnType, method, daoMeta);
        queryMeta.setResultParameterMeta(resultParameterMeta);
    }

    protected ResultParameterMeta createResultParameterMeta(
            AutoFunctionQueryMeta queryMeta, TypeMirror returnType,
            ExecutableElement method, DaoMeta daoMeta) {
        if (isList(returnType)) {
            DeclaredType listTyep = Models.toDeclaredType(returnType, env);
            List<? extends TypeMirror> args = listTyep.getTypeArguments();
            if (args.isEmpty()) {
                throw new AptException(MessageCode.DOMA4029, env, method);
            }
            TypeMirror elementType = Models.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), args.get(0));
            String elementTypeName = Models.getTypeName(elementType, daoMeta
                    .getTypeParameterMap(), env);
            if (isEntity(elementType, daoMeta)) {
                return new EntityListResultParameterMeta(elementTypeName);
            }
            if (isDomain(elementType)) {
                return new DomainListResultParameterMeta(elementTypeName);
            }
            throw new AptException(MessageCode.DOMA4065, env, method);
        }
        if (isDomain(returnType)) {
            return new DomainResultParameterMeta(queryMeta.getReturnTypeName());
        }
        throw new AptException(MessageCode.DOMA4063, env, method);
    }
}
