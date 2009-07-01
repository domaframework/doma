package org.seasar.doma.internal.apt.meta;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.apt.Models;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMetaFactory extends
        AbstractQueryMetaFactory {

    public AbstractCreateQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    protected TypeMirror getDomainValueType(TypeMirror domainType) {
        for (TypeMirror supertype : env.getTypeUtils()
                .directSupertypes(domainType)) {
            TypeElement typeElement = Models.toTypeElement(supertype, env);
            if (typeElement == null) {
                continue;
            }
            if (typeElement.getQualifiedName().contentEquals(Domain.class
                    .getName())) {
                DeclaredType declaredType = Models
                        .toDeclaredType(supertype, env);
                if (declaredType == null) {
                    continue;
                }
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                return args.get(0);
            }
            TypeMirror domainValueType = getDomainValueType(supertype);
            if (domainValueType != null) {
                return domainValueType;
            }
        }
        return null;
    }

}
