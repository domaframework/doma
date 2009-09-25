package org.seasar.doma.internal.apt.meta.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.Reference;

public class ReferenceType {

    protected TypeMirror type;

    protected String typeName;

    protected TypeMirror referentType;

    protected ValueType referentValueType;

    protected ReferenceType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public ValueType getReferentValueType() {
        return referentValueType;
    }

    public TypeMirror getReferentType() {
        return referentType;
    }

    public boolean isParametarized() {
        return referentType != null;
    }

    public static ReferenceType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType referenceDeclaredType = getReferenceDeclaredType(type, env);
        if (referenceDeclaredType == null) {
            return null;
        }
        ReferenceType referenceType = new ReferenceType();
        referenceType.type = type;
        referenceType.typeName = TypeUtil.getTypeName(type, env);
        List<? extends TypeMirror> typeArguments = referenceDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 1) {
            referenceType.referentType = typeArguments.get(0);
            referenceType.referentValueType = ValueType.newInstance(
                    referenceType.referentType, env);
        }
        return referenceType;
    }

    protected static DeclaredType getReferenceDeclaredType(TypeMirror type,
            ProcessingEnvironment env) {
        if (TypeUtil.isSameType(type, Reference.class, env)) {
            return TypeUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeUtil.isSameType(supertype, Reference.class, env)) {
                return TypeUtil.toDeclaredType(supertype, env);
            }
            getReferenceDeclaredType(supertype, env);
        }
        return null;
    }
}
