package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.ReferenceType;
import org.seasar.doma.internal.apt.type.SelectOptionsType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.message.DomaMessageCode;

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
        type = parameterElement.asType();
        typeName = TypeUtil.getTypeName(type, env);
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        if (typeElement != null) {
            qualifiedName = typeElement.getQualifiedName().toString();
        } else {
            qualifiedName = null;
        }
        dataType = createDataType(parameterElement, env);
    }

    protected DataType createDataType(VariableElement parameterElement,
            ProcessingEnvironment env) {
        ListType listType = ListType.newInstance(type, env);
        if (listType != null) {
            if (listType.isRawType()) {
                throw new AptException(DomaMessageCode.DOMA4108, env,
                        parameterElement, qualifiedName);
            }
            if (listType.isWildcardType()) {
                throw new AptException(DomaMessageCode.DOMA4112, env,
                        parameterElement, qualifiedName);
            }
            return listType;
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
                throw new AptException(DomaMessageCode.DOMA4110, env,
                        parameterElement, qualifiedName);
            }
            if (iterationCallbackType.isWildcardType()) {
                throw new AptException(DomaMessageCode.DOMA4112, env,
                        parameterElement, qualifiedName);
            }
            return iterationCallbackType;
        }

        ReferenceType referenceType = ReferenceType.newInstance(type, env);
        if (referenceType != null) {
            if (referenceType.isRaw()) {
                throw new AptException(DomaMessageCode.DOMA4108, env,
                        parameterElement, qualifiedName);
            }
            if (referenceType.isWildcardType()) {
                throw new AptException(DomaMessageCode.DOMA4112, env,
                        parameterElement, qualifiedName);
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
                    public Boolean visitListType(ListType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitReferenceType(ReferenceType dataType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

}
