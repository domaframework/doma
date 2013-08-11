/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.meta.DomainMeta;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainWrapper;

/**
 * 
 * @author taedium
 * 
 */
public class DomainTypeGenerator extends AbstractGenerator {

    protected final DomainMeta domainMeta;

    protected final String typeName;

    protected final String metaTypeName;

    protected final String simpleMetaTypeName;

    protected final String typeParamDecl;

    public DomainTypeGenerator(ProcessingEnvironment env,
            TypeElement domainElement, DomainMeta domainMeta)
            throws IOException {
        super(env, domainElement, null, null, Constants.METATYPE_PREFIX, "");
        assertNotNull(domainMeta);
        this.domainMeta = domainMeta;
        this.typeName = TypeMirrorUtil.getTypeName(domainMeta.getType(), env);
        this.metaTypeName = MetaUtil.getMetaTypeName(typeName);
        this.simpleMetaTypeName = MetaUtil.getSimpleMetaTypeName(typeName);
        this.typeParamDecl = makeTypeParamDecl(typeName);
    }

    private String makeTypeParamDecl(String typeName) {
        int pos = typeName.indexOf("<");
        if (pos == -1) {
            return "";
        }
        return typeName.substring(pos);
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    protected void printPackage() {
        if (!packageName.isEmpty()) {
            iprint("package %1$s;%n", packageName);
            iprint("%n");
        }
    }

    protected void printClass() {
        if (typeElement.getTypeParameters().isEmpty()) {
            iprint("/** */%n");
        } else {
            iprint("/**%n");
            for (TypeParameterElement typeParam : typeElement
                    .getTypeParameters()) {
                iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
            }
            iprint(" */%n");
        }
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s, %4$s> {%n",
                simpleMetaTypeName, DomainType.class.getName(),
                TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env),
                typeName);
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printConstructors();
        printMethods();
        unindent();
        unindent();
        iprint("}%n");
    }

    protected void printFields() {
        if (domainMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"rawtypes\")%n");
        }
        iprint("private static final %1$s singleton = new %1$s();%n",
                simpleName);
        print("%n");
    }

    protected void printConstructors() {
        iprint("private %1$s() {%n", simpleName);
        iprint("}%n");
        print("%n");
    }

    protected void printMethods() {
        printNewDomainMethod();
        printGetValueClassMethod();
        printGetDomainClassMethod();
        printGetWrapperMethod();
        printGetSingletonInternalMethod();
        printWrapperClass();
    }

    protected void printNewDomainMethod() {
        iprint("@Override%n");
        iprint("public %1$s newDomain(%2$s value) {%n", typeName,
                TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env));
        if (domainMeta.providesConstructor()) {
            if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
                iprint("    return new %1$s(%2$s.unbox(value));%n", typeName,
                        BoxedPrimitiveUtil.class.getName());
            } else {
                iprint("    return new %1$s(value);%n", typeName);
            }
        } else {
            if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
                iprint("    return %1$s.%2$s(%3$s.unbox(value));%n", domainMeta
                        .getTypeElement().getQualifiedName(),
                        domainMeta.getFactoryMethod(),
                        BoxedPrimitiveUtil.class.getName());
            } else {
                iprint("    return %1$s.%2$s(value);%n", domainMeta
                        .getTypeElement().getQualifiedName(),
                        domainMeta.getFactoryMethod());
            }
        }
        iprint("}%n");
        print("%n");
    }

    protected void printGetValueClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getValueClass() {%n",
                TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env));
        iprint("    return %1$s.class;%n",
                TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env));
        iprint("}%n");
        print("%n");
    }

    protected void printGetDomainClassMethod() {
        if (domainMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public Class<%1$s> getDomainClass() {%n", typeName);
        if (domainMeta.isParametarized()) {
            iprint("    Class<?> clazz = %1$s.class;%n", domainMeta
                    .getTypeElement().getQualifiedName());
            iprint("    return (Class<%1$s>) clazz;%n", typeName);
        } else {
            iprint("    return %1$s.class;%n", domainMeta.getTypeElement()
                    .getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    protected void printGetWrapperMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, %3$s> getWrapper(%3$s domain) {%n",
                DomainWrapper.class.getName(),
                TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(), env),
                typeName);
        iprint("    return new Wrapper%1$s(domain);%n", typeParamDecl);
        iprint("}%n");
        print("%n");
    }

    protected void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        if (domainMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
            iprint("public static %1$s %2$s getSingletonInternal() {%n",
                    typeParamDecl, simpleMetaTypeName);
            iprint("    return (%1$s) singleton;%n", simpleMetaTypeName);
        } else {
            iprint("public static %1$s getSingletonInternal() {%n",
                    simpleMetaTypeName);
            iprint("    return singleton;%n");
        }
        iprint("}%n");
        print("%n");
    }

    protected void printWrapperClass() {
        WrapperGenerator wrapperGenerator = createWrapperGenerator();
        wrapperGenerator.generate();
    }

    protected WrapperGenerator createWrapperGenerator() {
        return new WrapperGenerator();
    }

    protected class WrapperGenerator {

        protected void generate() {
            iprint("private static class Wrapper%1$s extends %2$s implements %3$s<%4$s, %5$s> {%n",
                    typeParamDecl, domainMeta.getWrapperType().getTypeName(),
                    DomainWrapper.class.getName(), TypeMirrorUtil
                            .boxIfPrimitive(domainMeta.getValueType(), env),
                    typeName);
            print("%n");
            indent();
            printWrapperField();
            printWrapperConstructor();
            printWrapperDoGetMethod();
            pirntWrapperDoSetMethod();
            printWrapperGetDomainMethod();
            unindent();
            iprint("}%n");
        }

        protected void printWrapperField() {
            iprint("private %1$s domain;%n", typeName);
            print("%n");
        }

        protected void printWrapperConstructor() {
            iprint("private Wrapper(%1$s domain) {%n", typeName);
            if (domainMeta.getWrapperType().getWrappedType().isEnum()) {
                iprint("    super(%1$s.class);%n",
                        TypeMirrorUtil.boxIfPrimitive(
                                domainMeta.getValueType(), env));
            }
            iprint("    this.domain = domain;%n");
            iprint("}%n");
            print("%n");
        }

        protected void printWrapperDoGetMethod() {
            iprint("@Override%n");
            iprint("protected %1$s doGet() {%n", TypeMirrorUtil.boxIfPrimitive(
                    domainMeta.getValueType(), env));
            iprint("    if (domain == null) {%n");
            iprint("        return null;%n");
            iprint("    }%n");
            iprint("    return domain.%1$s();%n",
                    domainMeta.getAccessorMethod());
            iprint("}%n");
            print("%n");
        }

        protected void pirntWrapperDoSetMethod() {
            iprint("@Override%n");
            iprint("protected void doSet(%1$s value) {%n",
                    TypeMirrorUtil.boxIfPrimitive(domainMeta.getValueType(),
                            env));
            if (domainMeta.providesConstructor()) {
                if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
                    iprint("    domain = new %1$s(%2$s.unbox(value));%n",
                            typeName, BoxedPrimitiveUtil.class.getName());
                } else {
                    iprint("    domain = new %1$s(value);%n", typeName);
                }
            } else {
                if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
                    iprint("    domain = %1$s.%2$s(%3$s.unbox(value));%n",
                            domainMeta.getTypeElement().getQualifiedName(),
                            domainMeta.getFactoryMethod(),
                            BoxedPrimitiveUtil.class.getName());
                } else {
                    iprint("    domain = %1$s.%2$s(value);%n", domainMeta
                            .getTypeElement().getQualifiedName(),
                            domainMeta.getFactoryMethod());
                }
            }
            iprint("}%n");
            print("%n");
        }

        protected void printWrapperGetDomainMethod() {
            iprint("@Override%n");
            iprint("public %1$s getDomain() {%n", typeName);
            iprint("    return domain;%n");
            iprint("}%n");
        }
    }
}
