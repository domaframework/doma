package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.ValueType;
import org.seasar.doma.message.DomaMessageCode;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement element;

    protected final TypeMirror type;

    protected final String typeName;

    protected ListType listType;

    protected EntityType entityType;

    protected DomainType domainType;

    protected ValueType valueType;

    public QueryReturnMeta(ExecutableElement methodElement,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, env);
        this.element = methodElement;
        this.env = env;
        type = methodElement.getReturnType();
        typeName = TypeUtil.getTypeName(type, env);

        listType = ListType.newInstance(type, env);
        if (listType == null) {
            entityType = EntityType.newInstance(type, env);
            if (entityType == null) {
                domainType = DomainType.newInstance(type, env);
                if (domainType == null) {
                    valueType = ValueType.newInstance(type, env);
                }
            }
        }

        if (listType != null) {
            if (listType.isRawType()) {
                throw new AptException(DomaMessageCode.DOMA4109, env,
                        methodElement, typeName);
            }
            if (listType.isWildcardType()) {
                throw new AptException(DomaMessageCode.DOMA4113, env,
                        methodElement, typeName);
            }
        }
    }

    public String getTypeName() {
        return typeName;
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

    public ListType getCollectionType() {
        return listType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isSupportedType() {
        return listType != null || entityType != null || domainType != null
                || valueType != null;
    }

}
