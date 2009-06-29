package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileSelectQueryMeta> {

    public SqlFileSelectQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileSelectQueryMeta queryMeta = createSqlFileSelectQueryMeta(method, daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFile(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected SqlFileSelectQueryMeta createSqlFileSelectQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        Select select = method.getAnnotation(Select.class);
        if (select == null) {
            return null;
        }
        SqlFileSelectQueryMeta queryMeta = new SqlFileSelectQueryMeta();
        queryMeta.setQueryTimeout(select.queryTimeout());
        queryMeta.setFetchSize(select.fetchSize());
        queryMeta.setMaxRows(select.maxRows());
        queryMeta.setIteration(select.iteration());
        queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    @Override
    protected void doReturnType(SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = Models.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), method.getReturnType());
        if (queryMeta.isIteration()) {
            TypeMirror wrapperType = Models
                    .toWrapperTypeIfPrimitive(returnType, env);
            if (returnType == null
                    || !env.getTypeUtils().isSameType(wrapperType, queryMeta
                            .getIterationCallbackResultType())) {
                throw new AptException(MessageCode.DOMA4055, env, method,
                        returnType, queryMeta.getIterationCallbackResultType());
            }
            queryMeta.setReturnTypeName(Models.getTypeName(returnType, daoMeta
                    .getTypeParameterMap(), env));
            return;
        }
        if (isList(returnType)) {
            DeclaredType listTyep = Models.toDeclaredType(returnType, env);
            List<? extends TypeMirror> args = listTyep.getTypeArguments();
            if (args.isEmpty()) {
                throw new AptException(MessageCode.DOMA4006, env, method);
            }
            TypeMirror elementType = Models.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), args.get(0));
            if (isEntity(elementType, daoMeta)) {
                queryMeta
                        .setEntityTypeName(Models
                                .getTypeName(elementType, daoMeta
                                        .getTypeParameterMap(), env));
            } else {
                if (isDomain(elementType) && !isAbstract(elementType)) {
                    queryMeta.setDomainTypeName(Models
                            .getTypeName(elementType, daoMeta
                                    .getTypeParameterMap(), env));
                } else {
                    throw new AptException(MessageCode.DOMA4007, env, method);
                }
            }
        } else {
            queryMeta.setSingleResult(true);
            if (isEntity(returnType, daoMeta)) {
                queryMeta
                        .setEntityTypeName(Models
                                .getTypeName(returnType, daoMeta
                                        .getTypeParameterMap(), env));
            } else {
                if (isDomain(returnType) && !isAbstract(returnType)) {
                    queryMeta.setDomainTypeName(Models
                            .getTypeName(returnType, daoMeta
                                    .getTypeParameterMap(), env));
                } else {
                    throw new AptException(MessageCode.DOMA4008, env, method);
                }
            }
        }
        queryMeta.setReturnTypeName(Models.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env));
    }

    @Override
    protected void doParameters(SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement param : method.getParameters()) {
            TypeMirror paramType = Models.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            String parameterName = Models.getParameterName(param);
            String parameterTypeName = Models.getTypeName(paramType, daoMeta
                    .getTypeParameterMap(), env);
            if (isOptions(paramType, queryMeta.getOptionsClass())) {
                if (queryMeta.getOptionsName() != null) {
                    throw new AptException(MessageCode.DOMA4053, env, method);
                }
                queryMeta.setOptionsName(parameterName);
                queryMeta.setOptionsTypeName(parameterTypeName);
            } else if (isIterationCallback(paramType)) {
                if (queryMeta.getIterationCallbackName() != null) {
                    throw new AptException(MessageCode.DOMA4054, env, method);
                }
                queryMeta.setIterationCallbackName(parameterName);
                queryMeta.setIterationCallbackTypeName(parameterTypeName);
                doIterationCallbackParameter(paramType, queryMeta, method, daoMeta);
            } else if (isList(paramType)) {
                DeclaredType listTyep = Models.toDeclaredType(paramType, env);
                List<? extends TypeMirror> args = listTyep.getTypeArguments();
                if (args.isEmpty()) {
                    throw new AptException(MessageCode.DOMA4027, env, method);
                }
                TypeMirror elementType = Models.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), args.get(0));
                if (!isDomain(elementType)) {
                    throw new AptException(MessageCode.DOMA4028, env, method);
                }
            } else if (!isDomain(paramType) && !isEntity(paramType, daoMeta)) {
                throw new AptException(MessageCode.DOMA4010, env, method);
            }
            queryMeta.addParameter(parameterName, parameterTypeName);
            queryMeta.addParameterType(parameterName, paramType);
        }
        if (queryMeta.isIteration()
                && queryMeta.getIterationCallbackName() == null) {
            throw new AptException(MessageCode.DOMA4056, env, method);
        }
        if (!queryMeta.isIteration()
                && queryMeta.getIterationCallbackName() != null) {
            throw new AptException(MessageCode.DOMA4057, env, method);
        }
    }

    protected void doIterationCallbackParameter(TypeMirror parameterType,
            SqlFileSelectQueryMeta queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        if (queryMeta.getIterationCallbackResultType() != null) {
            return;
        }
        TypeElement parameterElement = Models.toTypeElement(parameterType, env);
        if (parameterElement == null) {
            return;
        }
        if (parameterElement.getQualifiedName()
                .contentEquals(IterationCallback.class.getName())) {
            DeclaredType declaredType = Models
                    .toDeclaredType(parameterType, env);
            assertNotNull(declaredType);
            List<? extends TypeMirror> actualArgs = declaredType
                    .getTypeArguments();
            assertEquals(2, actualArgs.size());
            TypeMirror resultType = actualArgs.get(0);
            TypeMirror targetType = actualArgs.get(1);
            queryMeta.setIterationCallbackResultType(resultType);
            queryMeta.setIterationCallbackTargetType(targetType);
            String targetTypeName = Models.getTypeName(targetType, daoMeta
                    .getTypeParameterMap(), env);
            if (isDomain(targetType)) {
                queryMeta.setDomainTypeName(targetTypeName);
            } else if (isEntity(targetType, daoMeta)) {
                queryMeta.setEntityTypeName(targetTypeName);
            } else {
                throw new AptException(MessageCode.DOMA4058, env, method);
            }
            return;
        }
        for (TypeMirror supertype : env.getTypeUtils()
                .directSupertypes(parameterType)) {
            doIterationCallbackParameter(supertype, queryMeta, method, daoMeta);
        }
    }
}
