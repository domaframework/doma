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
                .getTypeElement().getQualifiedName(),
                TypeMirrorUtil.boxIfPrimitive(enumDomainMeta.getValueType(),
                        env));
        if (enumDomainMeta.getWrapperType().getWrappedType().isPrimitive()) {
            iprint("    return %1$s.%2$s(%3$s.unbox(value));%n", enumDomainMeta
                    .getTypeElement().getQualifiedName(),
                    enumDomainMeta.getFactoryMethod(),
                    BoxedPrimitiveUtil.class.getName());
        } else {
            iprint("    return %1$s.%2$s(value);%n", enumDomainMeta
                    .getTypeElement().getQualifiedName(),
                    enumDomainMeta.getFactoryMethod());
        }
        iprint("}%n");
        print("%n");
    }

    @Override
    protected WrapperGenerator createWrapperGenerator() {
        return new WrapperGenerator();
    }

    protected class WrapperGenerator extends
            DomainTypeGenerator.WrapperGenerator {

        @Override
        protected void pirntWrapperDoSetMethod() {
            iprint("@Override%n");
            iprint("protected void doSet(%1$s value) {%n",
                    TypeMirrorUtil.boxIfPrimitive(
                            enumDomainMeta.getValueType(), env));
            if (enumDomainMeta.getWrapperType().getWrappedType().isPrimitive()) {
                iprint("    domain = %1$s.%2$s(%3$s.unbox(value));%n",
                        enumDomainMeta.getTypeElement().getQualifiedName(),
                        enumDomainMeta.getFactoryMethod(),
                        BoxedPrimitiveUtil.class.getName());
            } else {
                iprint("    domain = %1$s.%2$s(value);%n", enumDomainMeta
                        .getTypeElement().getQualifiedName(),
                        enumDomainMeta.getFactoryMethod());
            }
            iprint("}%n");
            print("%n");
        }
    }
}
