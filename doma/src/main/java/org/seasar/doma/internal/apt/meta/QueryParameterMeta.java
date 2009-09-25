package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.apt.meta.type.CollectionType;
import org.seasar.doma.internal.apt.meta.type.DomainType;
import org.seasar.doma.internal.apt.meta.type.EntityType;
import org.seasar.doma.internal.apt.meta.type.IterationCallbackType;
import org.seasar.doma.internal.apt.meta.type.ReferenceType;
import org.seasar.doma.internal.apt.meta.type.SelectOptionsType;
import org.seasar.doma.internal.apt.meta.type.ValueType;
import org.seasar.doma.message.DomaMessageCode;

public class QueryParameterMeta {

    protected final VariableElement element;

    protected final ProcessingEnvironment env;

    protected final String name;

    protected final String typeName;

    protected final String qualifiedName;

    protected final TypeMirror type;

    protected CollectionType collectionType;

    protected EntityType entityType;

    protected DomainType domainType;

    protected ValueType valueType;

    protected SelectOptionsType selectOptionsType;

    protected IterationCallbackType iterationCallbackType;

    protected ReferenceType referenceType;

    public QueryParameterMeta(VariableElement parameterElement,
            ProcessingEnvironment env) {
        assertNotNull(parameterElement, env);
        this.element = parameterElement;
        this.env = env;
        name = ElementUtil.getParameterName(parameterElement);
        type = parameterElement.asType();
        typeName = TypeUtil.getTypeName(type, env);
        qualifiedName = TypeUtil.getTypeName(env.getTypeUtils().erasure(type),
                env);

        collectionType = CollectionType.newInstance(type, env);
        if (collectionType == null) {
            entityType = EntityType.newInstance(type, env);
            if (entityType == null) {
                domainType = DomainType.newInstance(type, env);
                if (domainType == null) {
                    valueType = ValueType.newInstance(type, env);
                    if (valueType == null) {
                        selectOptionsType = SelectOptionsType.newInstance(type,
                                env);
                        if (selectOptionsType == null) {
                            iterationCallbackType = IterationCallbackType
                                    .newInstance(type, env);
                            if (iterationCallbackType == null) {
                                referenceType = ReferenceType.newInstance(type,
                                        env);
                            }
                        }
                    }
                }
            }
        }

        if (collectionType != null && !collectionType.isParametarized()) {
            throw new AptException(DomaMessageCode.DOMA4108, env,
                    parameterElement, qualifiedName);
        }
        if (iterationCallbackType != null
                && !iterationCallbackType.isParametarized()) {
            throw new AptException(DomaMessageCode.DOMA4110, env,
                    parameterElement, qualifiedName);
        }
        if (referenceType != null && !referenceType.isParametarized()) {
            throw new AptException(DomaMessageCode.DOMA4099, env,
                    parameterElement, qualifiedName);
        }
    }

    public VariableElement getElement() {
        return element;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public CollectionType getCollectionType() {
        return collectionType;
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

    public SelectOptionsType getSelectOptionsType() {
        return selectOptionsType;
    }

    public IterationCallbackType getIterationCallbackType() {
        return iterationCallbackType;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public boolean isNullable() {
        return valueType != null;
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

}
