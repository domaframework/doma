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
package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.apt.generator.CodeHelper.wrapperSupplier;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Formatter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.holder.HolderMeta;
import org.seasar.doma.internal.util.BoxedPrimitiveUtil;
import org.seasar.doma.jdbc.holder.AbstractHolderDesc;

/**
 * 
 * @author taedium
 * 
 */
public class HolderDescGenerator extends AbstractGenerator {

    private final HolderMeta holderMeta;

    private final String typeName;

    public HolderDescGenerator(Context ctx, HolderMeta holderMeta, CodeSpec codeSpec,
            Formatter formatter) {
        super(ctx, codeSpec, formatter);
        assertNotNull(holderMeta);
        this.holderMeta = holderMeta;
        this.typeName = ctx.getTypes().getTypeName(holderMeta.getType());
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    private void printClass() {
        TypeElement typeElement = holderMeta.getHolderElement();
        if (typeElement.getTypeParameters().isEmpty()) {
            iprint("/** */%n");
        } else {
            iprint("/**%n");
            for (TypeParameterElement typeParam : typeElement.getTypeParameters()) {
                iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
            }
            iprint(" */%n");
        }
        printGenerated();
        if (holderMeta.isParametarized()) {
            iprint("public final class %1$s<%5$s> extends %2$s<%3$s, %4$s> {%n",
                    // @formatter:off
                    /* 1 */codeSpec.getSimpleName(),
                    /* 2 */AbstractHolderDesc.class.getName(),
                    /* 3 */ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
                    /* 4 */typeName,
                    /* 5 */codeSpec.getTypeParamsName());
                    // @formatter:on
        } else {
            iprint("public final class %1$s extends %2$s<%3$s, %4$s> {%n",
                    // @formatter:off
                    /* 1 */codeSpec.getSimpleName(),
                    /* 2 */AbstractHolderDesc.class.getName(),
                    /* 3 */ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
                    /* 4 */typeName);
                    // @formatter:on
        }
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

    private void printFields() {
        if (holderMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"rawtypes\")%n");
        }
        iprint("private static final %1$s singleton = new %1$s();%n", codeSpec.getSimpleName());
        print("%n");
    }

    private void printConstructors() {
        iprint("private %1$s() {%n", codeSpec.getSimpleName());
        BasicCtType basicCtType = holderMeta.getBasicCtType();
        iprint("    super(%1$s);%n", wrapperSupplier(basicCtType));
        iprint("}%n");
        print("%n");
    }

    private void printMethods() {
        printNewHolderMethod();
        printGetBasicValueMethod();
        printGetBasicClassMethod();
        printGetHolderClassMethod();
        printGetSingletonInternalMethod();
    }

    private void printNewHolderMethod() {
        boolean primitive = holderMeta.getBasicCtType().isPrimitive();
        iprint("@Override%n");
        iprint("protected %1$s newHolder(%2$s value) {%n",
                // @formatter:off
                /* 1 */typeName,
                /* 2 */ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()));
                // @formatter:on
        if (!primitive && !holderMeta.getAcceptNull()) {
            iprint("    if (value == null) {%n");
            iprint("        return null;%n");
            iprint("    }%n");
        }
        if (holderMeta.providesConstructor()) {
            if (primitive) {
                iprint("    return new %1$s(%2$s.unbox(value));%n",
                        // @formatter:off
                        /* 1 */typeName,
                        /* 2 */BoxedPrimitiveUtil.class.getName());
                        // @formatter:on
            } else {
                iprint("    return new %1$s(value);%n",
                        // @formatter:off
                        /* 1 */typeName);
                        // @formatter:on
            }
        } else {
            if (primitive) {
                iprint("    return %1$s.%2$s(%3$s.unbox(value));%n",
                        // @formatter:off
                        /* 1 */holderMeta.getHolderElement().getQualifiedName(),
                        /* 2 */holderMeta.getFactoryMethod(),
                        /* 3 */BoxedPrimitiveUtil.class.getName());
                        // @formatter:on
            } else {
                iprint("    return %1$s.%2$s(value);%n",
                        // @formatter:off
                        /* 1 */holderMeta.getHolderElement().getQualifiedName(),
                        /* 2 */holderMeta.getFactoryMethod());
                        // @formatter:on
            }
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetBasicValueMethod() {
        iprint("@Override%n");
        iprint("protected %1$s getBasicValue(%2$s holder) {%n",
                // @formatter:off
                /* 1 */ctx.getTypes().boxIfPrimitive(holderMeta.getValueType()),
                /* 2 */typeName);
                // @formatter:on
        iprint("    if (holder == null) {%n");
        iprint("        return null;%n");
        iprint("    }%n");
        iprint("    return holder.%1$s();%n", holderMeta.getAccessorMethod());
        iprint("}%n");
        print("%n");
    }

    private void printGetBasicClassMethod() {
        iprint("@Override%n");
        iprint("public Class<?> getBasicClass() {%n");
        iprint("    return %1$s.class;%n", holderMeta.getValueType());
        iprint("}%n");
        print("%n");
    }

    private void printGetHolderClassMethod() {
        if (holderMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public Class<%1$s> getHolderClass() {%n", typeName);
        if (holderMeta.isParametarized()) {
            iprint("    Class<?> clazz = %1$s.class;%n",
                    holderMeta.getHolderElement().getQualifiedName());
            iprint("    return (Class<%1$s>) clazz;%n", typeName);
        } else {
            iprint("    return %1$s.class;%n", holderMeta.getHolderElement().getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        if (holderMeta.isParametarized()) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
            iprint("public static <%1$s> %2$s<%1$s> getSingletonInternal() {%n",
                    // @formatter:off
                    /* 1 */codeSpec.getTypeParamsName(),
                    /* 2 */codeSpec.getSimpleName());
                    // @formatter:on
            iprint("    return (%2$s<%1$s>) singleton;%n",
                    // @formatter:off
                    /* 1 */codeSpec.getTypeParamsName(), 
                    /* 2 */codeSpec.getSimpleName());
                    // @formatter:on
        } else {
            iprint("public static %1$s getSingletonInternal() {%n", codeSpec.getSimpleName());
            iprint("    return singleton;%n");
        }
        iprint("}%n");
        print("%n");
    }
}
