package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.Message;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement element;

    protected final TypeMirror type;

    protected final String typeName;

    protected final DataType dataType;

    public QueryReturnMeta(ExecutableElement methodElement,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, env);
        this.element = methodElement;
        this.env = env;
        type = methodElement.getReturnType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        dataType = createDataType(methodElement, type, env);
    }

    protected DataType createDataType(ExecutableElement methodElement,
            TypeMirror type, ProcessingEnvironment env) {
        IterableType iterableType = IterableType.newInstance(type, env);
        if (iterableType != null) {
            if (iterableType.isRawType()) {
                throw new AptException(Message.DOMA4109, env, methodElement,
                        typeName);
            }
            if (iterableType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, env, methodElement,
                        typeName);
            }
            return iterableType;
        }

        EntityType entityType = EntityType.newInstance(type, env);
        if (entityType != null) {
            return entityType;
        }

        DomainType domainType = DomainType.newInstance(type, env);
        if (domainType != null) {
            return domainType;
        }

        BasicType basicType = BasicType.newInstance(type, env);
        if (basicType != null) {
            return basicType;
        }

        return AnyType.newInstance(type, env);
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeNameAsTypeParameter() {
        return dataType.getTypeNameAsTypeParameter();
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

    public ExecutableElement getElement() {
        return element;
    }

    public TypeMirror getType() {
        return type;
    }

    public DataType getDataType() {
        return dataType;
    }

}
