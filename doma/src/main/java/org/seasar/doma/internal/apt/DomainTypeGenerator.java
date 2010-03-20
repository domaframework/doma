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

import org.seasar.doma.internal.apt.meta.DomainMeta;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainWrapper;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;

/**
 * 
 * @author taedium
 * 
 */
public class DomainTypeGenerator extends AbstractGenerator {

    protected final DomainMeta domainMeta;

    public DomainTypeGenerator(ProcessingEnvironment env,
            TypeElement domainElement, DomainMeta domainMeta)
            throws IOException {
        super(env, domainElement, null, null, Constants.DEFAULT_DOMAIN_PREFIX,
                "");
        assertNotNull(domainMeta);
        this.domainMeta = domainMeta;
    }

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
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s, %4$s> {%n",
                simpleName, DomainType.class.getName(), TypeMirrorUtil
                        .boxIfPrimitive(domainMeta.getValueType(), env),
                domainMeta.getTypeElement().getQualifiedName());
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
        printGetDomainClassMethod();
        printGetWrapperMethod();
        printGetSingletonInternalMethod();
        printWrapperClass();
    }

    protected void printNewDomainMethod() {
        iprint("@Override%n");
        iprint("public %1$s newDomain(%2$s value) {%n", domainMeta
                .getTypeElement().getQualifiedName(), TypeMirrorUtil
                .boxIfPrimitive(domainMeta.getValueType(), env));
        if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("    return new %1$s(%2$s.unbox(value));%n", domainMeta
                    .getTypeElement().getQualifiedName(),
                    BoxedPrimitiveUtil.class.getName());
        } else {
            iprint("    return new %1$s(value);%n", domainMeta.getTypeElement()
                    .getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    protected void printGetDomainClassMethod() {
        iprint("@Override%n");
        iprint("public Class<%1$s> getDomainClass() {%n", domainMeta
                .getTypeElement().getQualifiedName());
        iprint("    return %1$s.class;%n", domainMeta.getTypeElement()
                .getQualifiedName());
        iprint("}%n");
        print("%n");
    }

    protected void printGetWrapperMethod() {
        iprint("@Override%n");
        iprint("public %1$s<%2$s, %3$s> getWrapper(%3$s domain) {%n",
                DomainWrapper.class.getName(), TypeMirrorUtil.boxIfPrimitive(
                        domainMeta.getValueType(), env), domainMeta
                        .getTypeElement().getQualifiedName());
        iprint("    return new Wrapper(domain);%n");
        iprint("}%n");
        print("%n");
    }

    protected void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", simpleName);
        iprint("    return singleton;%n");
        iprint("}%n");
        print("%n");
    }

    protected void printWrapperClass() {
        iprint(
                "private static class Wrapper extends %1$s implements %2$s<%3$s, %4$s> {%n",
                domainMeta.getWrapperType().getTypeName(), DomainWrapper.class
                        .getName(), TypeMirrorUtil.boxIfPrimitive(domainMeta
                        .getValueType(), env), domainMeta.getTypeElement()
                        .getQualifiedName());
        print("%n");
        iprint("    private %1$s domain;%n", domainMeta.getTypeElement()
                .getQualifiedName());
        print("%n");
        iprint("    private Wrapper(%1$s domain) {%n", domainMeta
                .getTypeElement().getQualifiedName());
        if (domainMeta.getWrapperType().getWrappedType().isEnum()) {
            iprint("        super(%1$s.class);%n", TypeMirrorUtil
                    .boxIfPrimitive(domainMeta.getValueType(), env));
        }
        iprint("        if (domain == null) {%n");
        iprint("            this.domain = new %1$s((%2$s) %3$s);%n", domainMeta
                .getTypeElement().getQualifiedName(),
                domainMeta.getValueType(), domainMeta.getWrapperType()
                        .getWrappedType().getDefaultValue());
        iprint("        } else {%n");
        iprint("            this.domain = domain;%n");
        iprint("        }%n");
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    protected %1$s doGet() {%n", TypeMirrorUtil.boxIfPrimitive(
                domainMeta.getValueType(), env));
        iprint("        return domain.%1$s();%n", domainMeta
                .getAccessorMethod());
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    protected void doSet(%1$s value) {%n", TypeMirrorUtil
                .boxIfPrimitive(domainMeta.getValueType(), env));
        if (domainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("        domain = new %1$s(%2$s.unbox(value));%n",
                    domainMeta.getTypeElement().getQualifiedName(),
                    BoxedPrimitiveUtil.class.getName());
        } else {
            iprint("        domain = new %1$s(value);%n", domainMeta
                    .getTypeElement().getQualifiedName());
        }
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    public %1$s getDomain() {%n", domainMeta.getTypeElement()
                .getQualifiedName());
        iprint("        return domain;%n");
        iprint("    }%n");
        iprint("}%n");
    }
}
