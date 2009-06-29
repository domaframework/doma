package org.seasar.doma.internal.apt.meta;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory extends
        AbstractQueryMetaFactory {

    public AutoModuleQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    protected void doParameters(AutoModuleQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement param : method.getParameters()) {
            TypeMirror paramType = Models.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            String typeName = Models.getTypeName(paramType, daoMeta
                    .getTypeParameterMap(), env);
            String name = Models.getParameterName(param);
            CallableStatementParameterMeta parameterMeta = createParameterMeta(queryMeta, param, method, daoMeta);
            parameterMeta.setName(name);
            parameterMeta.setTypeName(typeName);
            queryMeta.addCallableStatementParameterMeta(parameterMeta);
        }
    }

    protected CallableStatementParameterMeta createParameterMeta(AutoModuleQueryMeta queryMeta,
            VariableElement param, ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror paramType = Models.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), param.asType());
        if (param.getAnnotation(ResultSet.class) != null) {
            if (isList(paramType)) {
                DeclaredType listTyep = Models.toDeclaredType(paramType, env);
                List<? extends TypeMirror> args = listTyep.getTypeArguments();
                if (args.isEmpty()) {
                    throw new AptException(MessageCode.DOMA4041, env, method);
                }
                TypeMirror elementType = Models.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), args.get(0));
                String elementTypeName = Models
                        .getTypeName(elementType, daoMeta.getTypeParameterMap(), env);
                if (isEntity(elementType, daoMeta)) {
                    return new EntityListParameterMeta(elementTypeName);
                }
                if (isDomain(elementType)) {
                    return new DomainListParameterMeta(elementTypeName);
                }
                throw new AptException(MessageCode.DOMA4061, env, param);
            }
            throw new AptException(MessageCode.DOMA4062, env, param);
        }
        if (!isDomain(paramType)) {
            throw new AptException(MessageCode.DOMA4060, env, param);
        }
        if (param.getAnnotation(Out.class) != null) {
            return new OutParameterMeta();
        }
        if (param.getAnnotation(InOut.class) != null) {
            return new InOutParameterMeta();
        }
        if (param.getAnnotation(In.class) != null) {
            return new InParameterMeta();
        }
        throw new AptException(MessageCode.DOMA4066, env, param);
    }
}
