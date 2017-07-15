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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddablePropertyMeta;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EmbeddableDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableDescGenerator extends AbstractGenerator {

    private final EmbeddableMeta embeddableMeta;

    public EmbeddableDescGenerator(Context ctx, EmbeddableMeta embeddableMeta, CodeSpec codeSpec,
            Formatter formatter) {
        super(ctx, codeSpec, formatter);
        assertNotNull(embeddableMeta);
        this.embeddableMeta = embeddableMeta;
    }

    @Override
    public void generate() {
        printPackage();
        printClass();
    }

    private void printClass() {
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s> {%n",
                // @formatter:off
                /* 1 */simpleName,
                /* 2 */EmbeddableDesc.class.getName(),
                /* 3 */embeddableMeta.getEmbeddableElement().getQualifiedName());
                // @formatter:on
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printMethods();
        unindent();
        iprint("}%n");
    }

    private void printFields() {
        printSingletonField();
    }

    private void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n", simpleName);
        print("%n");
    }

    private void printMethods() {
        printGetEmbeddablePropertyDescsMethod();
        printNewEmbeddableMethod();
        printGetSingletonInternalMethod();
    }

    private void printGetEmbeddablePropertyDescsMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyDescs"
                + "(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType) {%n",
                List.class.getName(), EntityPropertyDesc.class.getName(),
                NamingType.class.getName());
        iprint("    return %1$s.asList(%n", Arrays.class.getName());
        for (Iterator<EmbeddablePropertyMeta> it = embeddableMeta.getEmbeddablePropertyMetas()
                .iterator(); it.hasNext();) {
            EmbeddablePropertyMeta pm = it.next();
            EmbeddablePropertyCtTypeVisitor visitor = new EmbeddablePropertyCtTypeVisitor();
            pm.getCtType().accept(visitor, null);
            BasicCtType basicCtType = visitor.basicCtType;
            HolderCtType holderCtType = visitor.holderCtType;

            String holderType = "null";
            String holderTypeName = "Object";
            if (holderCtType != null) {
                holderType = holderCtType.getInstantiationCode();
                holderTypeName = holderCtType.getTypeName();
            }
            iprint("        new %1$s<ENTITY, %2$s, %3$s>(entityClass, %4$s, %5$s, "
                    + "embeddedPropertyName + \".%6$s\", \"%7$s\", namingType, %8$s, %9$s, %10$s)",
                    // @formatter:off
                    /* 1 */DefaultPropertyDesc.class.getName(),
                    /* 2 */box(basicCtType),
                    /* 3 */holderTypeName,
                    /* 4 */supply(basicCtType),
                    /* 5 */holderType,
                    /* 6 */pm.getName(),
                    /* 7 */pm.getColumnName(),
                    /* 8 */pm.isColumnInsertable(),
                    /* 9 */pm.isColumnUpdatable(),
                    /* 10 */pm.isColumnQuoteRequired());
                    // @formatter:on
            print(it.hasNext() ? ",%n" : "");
        }
        print(");%n");
        iprint("}%n");
        print("%n");
    }

    private void printNewEmbeddableMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
                embeddableMeta.getEmbeddableElement().getQualifiedName(), Map.class.getName(),
                Property.class.getName());
        if (embeddableMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            iprint("    return new %1$s(%n",
                    embeddableMeta.getEmbeddableElement().getQualifiedName());
            for (Iterator<EmbeddablePropertyMeta> it = embeddableMeta.getEmbeddablePropertyMetas()
                    .iterator(); it.hasNext();) {
                EmbeddablePropertyMeta propertyMeta = it.next();
                iprint("        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") != null "
                        + "? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
                        // @formatter:off
                        /* 1 */box(propertyMeta.getTypeName()),
                        /* 2 */propertyMeta.getName());
                        // @formatter:on
                if (it.hasNext()) {
                    print(",%n");
                }
            }
            print(");%n");
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", simpleName);
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    private class EmbeddablePropertyCtTypeVisitor
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private BasicCtType basicCtType;

        private HolderCtType holderCtType;

        @Override
        protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
            assertNotNull(basicCtType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
                throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitBasicCtType(BasicCtType basicCtType, Void p) throws RuntimeException {
            this.basicCtType = basicCtType;
            return defaultAction(basicCtType, p);
        }

        @Override
        public Void visitHolderCtType(HolderCtType holderCtType, Void p) throws RuntimeException {
            this.holderCtType = holderCtType;
            return visitBasicCtType(holderCtType.getBasicCtType(), p);
        }
    }

}
