package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.ReferenceType;
import org.seasar.doma.internal.apt.type.SelectOptionsType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.Message;

public class QueryParameterMeta {

    protected final VariableElement element;

    protected final ProcessingEnvironment env;

    protected final String name;

    protected final String typeName;

    protected final TypeMirror type;

    protected final String qualifiedName;

    protected final DataType dataType;

    public QueryParameterMeta(VariableElement parameterElement,
            ProcessingEnvironment env) {
        assertNotNull(parameterElement, env);
        this.element = parameterElement;
        this.env = env;
        name = ElementUtil.getParameterName(parameterElement);
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, env, parameterElement,
                    MetaConstants.RESERVED_NAME_PREFIX);
        }
        type = parameterElement.asType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement != null) {
            qualifiedName = typeElement.getQualifiedName().toString();
        } else {
            qualifiedName = typeName;
        }
        dataType = createDataType(parameterElement, env);
    }

    protected DataType createDataType(VariableElement parameterElement,
            ProcessingEnvironment env) {
        IterableType iterableType = IterableType.newInstance(type, env);
        if (iterableType != null) {
            if (iterableType.isRawType()) {
                throw new AptException(Message.DOMA4159, env, parameterElement);
            }
            if (iterableType.isWildcardType()) {
                throw new AptException(Message.DOMA4160, env, parameterElement);
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

        SelectOptionsType selectOptionsType = SelectOptionsType.newInstance(
                type, env);
        if (selectOptionsType != null) {
            return selectOptionsType;
        }

        IterationCallbackType iterationCallbackType = IterationCallbackType
                .newInstance(type, env);
        if (iterationCallbackType != null) {
            if (iterationCallbackType.isRawType()) {
                throw new AptException(Message.DOMA4110, env, parameterElement,
                        qualifiedName);
            }
            if (iterationCallbackType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            return iterationCallbackType;
        }

        ReferenceType referenceType = ReferenceType.newInstance(type, env);
        if (referenceType != null) {
            if (referenceType.isRaw()) {
                throw new AptException(Message.DOMA4108, env, parameterElement,
                        qualifiedName);
            }
            if (referenceType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            return referenceType;
        }

        return AnyType.newInstance(type, env);
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

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return dataType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicType(BasicType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainType(DomainType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isBindable() {
        return dataType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicType(BasicType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainType(DomainType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitEntityType(EntityType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitIterableType(IterableType dataType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitReferenceType(ReferenceType dataType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitAnyType(AnyType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

}
