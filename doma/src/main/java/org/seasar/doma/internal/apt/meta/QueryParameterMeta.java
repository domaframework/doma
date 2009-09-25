package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Collections;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

public class QueryParameterMeta {

    private final VariableElement parameterElement;

    private final ProcessingEnvironment env;

    private final String name;

    private final TypeMirror type;

    private final String typeName;

    private final TypeElement typeElement;

    private boolean collection;

    private final boolean basic;

    private TypeMirror collectionElementType;

    private String collectionElementTypeName;

    public QueryParameterMeta(VariableElement parameterElement,
            ProcessingEnvironment env) {
        assertNotNull(parameterElement, env);
        this.parameterElement = parameterElement;
        this.env = env;
        this.name = ElementUtil.getParameterName(parameterElement);
        this.type = parameterElement.asType();
        this.typeName = TypeUtil.getTypeName(type, env);
        this.typeElement = TypeUtil.toTypeElement(type, env);

        if (typeElement != null) {
            DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
            List<? extends TypeParameterElement> typeParams = typeElement
                    .getTypeParameters();
            List<? extends TypeMirror> typeArgs = declaredType
                    .getTypeArguments();
            if (typeParams.size() > 0 && typeParams.size() != typeArgs.size()) {
                throw new AptException(DomaMessageCode.DOMA4108, env,
                        parameterElement, typeElement.getQualifiedName());
            }

            collection = TypeUtil.isCollection(declaredType, env);
            if (collection) {
                collectionElementType = typeArgs.get(0);
                collectionElementTypeName = TypeUtil.getTypeName(
                        collectionElementType, env);
            }
        }

        this.basic = DomaTypes.isSupportedType(type, env);
    }

    public VariableElement getParameterElement() {
        return parameterElement;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public boolean isCollection() {
        return collection;
    }

    public TypeMirror getCollectionElementType() {
        return collectionElementType;
    }

    public String getCollectionElementTypeName() {
        return collectionElementTypeName;
    }

    public void setCollectionElementTypeName(String collectionElementTypeName) {
        this.collectionElementTypeName = collectionElementTypeName;
    }

    public boolean isEntity() {
        return TypeUtil.isEntity(type, env);
    }

    public boolean isDomain() {
        return TypeUtil.isDomain(type, env);
    }

    public boolean isSelectOptions() {
        return TypeUtil.isSelectOptions(type, env);
    }

    public boolean isIterationCallback() {
        return TypeUtil.isIterationCallback(type, env);
    }

    public boolean isBasic() {
        return basic;
    }

    public boolean isNullable() {
        return basic;
    }

    public List<? extends TypeMirror> getTypeArguments() {
        DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
        if (declaredType != null) {
            return declaredType.getTypeArguments();
        }
        return Collections.emptyList();
    }
}
