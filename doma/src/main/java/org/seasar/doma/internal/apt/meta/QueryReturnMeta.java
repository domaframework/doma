package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final TypeMirror type;

    protected final String typeName;

    protected final TypeElement typeElement;

    protected final boolean entity;

    protected final TypeMirror wrapperType;

    protected String wrapperTypeName;

    protected boolean collection;

    protected TypeMirror collectionElementType;

    protected String collectionElementTypeName;

    protected boolean collectionElementEntity;

    protected TypeMirror collectionElementWrapperType;

    protected String collectionElementWrapperTypeName;

    public QueryReturnMeta(ExecutableElement methodElement,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, env);
        this.methodElement = methodElement;
        this.env = env;
        type = methodElement.getReturnType();
        typeName = TypeUtil.getTypeName(type, env);
        typeElement = TypeUtil.toTypeElement(type, env);
        entity = TypeUtil.isEntity(type, env);
        wrapperType = DomaTypes.getWrapperType(type, env);
        if (wrapperType != null) {
            wrapperTypeName = TypeUtil.getTypeName(wrapperType, env);
        }

        DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
        if (typeElement != null && declaredType != null) {
            List<? extends TypeParameterElement> typeParams = typeElement
                    .getTypeParameters();
            List<? extends TypeMirror> typeArgs = declaredType
                    .getTypeArguments();
            if (typeParams.size() > 0 && typeParams.size() != typeArgs.size()) {
                throw new AptException(DomaMessageCode.DOMA4109, env,
                        methodElement, typeElement.getQualifiedName());
            }
            collection = TypeUtil.isCollection(declaredType, env);
            if (collection) {
                collectionElementType = typeArgs.get(0);
                collectionElementTypeName = TypeUtil.getTypeName(
                        collectionElementType, env);
                if (TypeUtil.isEntity(collectionElementType, env)) {
                    collectionElementEntity = true;
                } else {
                    collectionElementWrapperType = DomaTypes.getWrapperType(
                            collectionElementType, env);
                    if (collectionElementWrapperType != null) {
                        collectionElementWrapperTypeName = TypeUtil
                                .getTypeName(collectionElementWrapperType, env);
                    }
                }
            }
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    public String getCollectionElementWrapperTypeName() {
        return collectionElementWrapperTypeName;
    }

    public boolean isCollection() {
        return collection;
    }

    public boolean isEntity() {
        return entity;
    }

    public boolean isPrimitiveInt() {
        return type.getKind() == TypeKind.INT;
    }

    public boolean isPrimitiveIntArray() {
        return type.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return t.getComponentType().getKind() == TypeKind.INT;
            }
        }, null);
    }

    public boolean isPrimitiveVoid() {
        return type.getKind() == TypeKind.VOID;
    }

    public TypeMirror getCollectionElementType() {
        return collectionElementType;
    }

    public String getCollectionElementTypeName() {
        return collectionElementTypeName;
    }

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public boolean isCollectionElementEntity() {
        return collectionElementEntity;
    }

    public TypeMirror getWrapperType() {
        return wrapperType;
    }

    public TypeMirror getCollectionElementWrapperType() {
        return collectionElementWrapperType;
    }

}
