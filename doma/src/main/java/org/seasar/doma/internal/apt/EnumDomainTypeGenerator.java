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

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.EnumDomainMeta;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.domain.DomainWrapper;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;

/**
 * 
 * @author taedium
 * 
 */
public class EnumDomainTypeGenerator extends DomainTypeGenerator {

    protected final EnumDomainMeta enumDomainMeta;

    public EnumDomainTypeGenerator(ProcessingEnvironment env,
            TypeElement domainElement, EnumDomainMeta enumDomainMeta)
            throws IOException {
        super(env, domainElement, enumDomainMeta);
        this.enumDomainMeta = enumDomainMeta;
    }

    @Override
    protected void printNewDomainMethod() {
        iprint("@Override%n");
        iprint("public %1$s newDomain(%2$s value) {%n", enumDomainMeta
                .getTypeElement().getQualifiedName(), TypeMirrorUtil
                .boxIfPrimitive(enumDomainMeta.getValueType(), env));
        if (enumDomainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("    return %1$s.%2$s(%3$s.unbox(value));%n", enumDomainMeta
                    .getTypeElement().getQualifiedName(), enumDomainMeta
                    .getFactoryMethod(), BoxedPrimitiveUtil.class.getName());
        } else {
            iprint("    return %1$s.%2$s(value);%n", enumDomainMeta
                    .getTypeElement().getQualifiedName(), enumDomainMeta
                    .getFactoryMethod());
        }
        iprint("}%n");
        print("%n");
    }

    @Override
    protected void printWrapperClass() {
        iprint(
                "private static class Wrapper extends %1$s implements %2$s<%3$s, %4$s> {%n",
                enumDomainMeta.getWrapperType().getTypeName(),
                DomainWrapper.class.getName(), TypeMirrorUtil.boxIfPrimitive(
                        enumDomainMeta.getValueType(), env), enumDomainMeta
                        .getTypeElement().getQualifiedName());
        print("%n");
        iprint("    private %1$s domain;%n", enumDomainMeta.getTypeElement()
                .getQualifiedName());
        print("%n");
        iprint("    private Wrapper(%1$s domain) {%n", enumDomainMeta
                .getTypeElement().getQualifiedName());
        if (enumDomainMeta.getWrapperType().getWrappedType().isEnum()) {
            iprint("        super(%1$s.class);%n", TypeMirrorUtil
                    .boxIfPrimitive(enumDomainMeta.getValueType(), env));
        }
        iprint("        this.domain = domain;%n");
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    protected %1$s doGet() {%n", TypeMirrorUtil.boxIfPrimitive(
                enumDomainMeta.getValueType(), env));
        iprint("        if (domain == null) {%n");
        if (enumDomainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("            return %1$s.unbox(value);%n",
                    BoxedPrimitiveUtil.class.getName());
        } else {
            iprint("            return null;%n");
        }
        iprint("        }%n");
        iprint("        return domain.%1$s();%n", enumDomainMeta
                .getAccessorMethod());
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    protected void doSet(%1$s value) {%n", TypeMirrorUtil
                .boxIfPrimitive(enumDomainMeta.getValueType(), env));
        if (enumDomainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("        domain = %1$s.%2$s(%3$s.unbox(value));%n",
                    enumDomainMeta.getTypeElement().getQualifiedName(),
                    enumDomainMeta.getFactoryMethod(), BoxedPrimitiveUtil.class
                            .getName());
        } else {
            iprint("        domain = %1$s.%2$s(value);%n", enumDomainMeta
                    .getTypeElement().getQualifiedName(), enumDomainMeta
                    .getFactoryMethod());
        }
        iprint("    }%n");
        print("%n");
        iprint("    @Override%n");
        iprint("    public %1$s getDomain() {%n", enumDomainMeta
                .getTypeElement().getQualifiedName());
        iprint("        return domain;%n");
        iprint("    }%n");
        iprint("}%n");
    }
}
