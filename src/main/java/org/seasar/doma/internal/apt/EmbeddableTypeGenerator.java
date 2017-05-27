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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.meta.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.EmbeddablePropertyMeta;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableTypeGenerator extends AbstractGenerator {

    protected final EmbeddableMeta embeddableMeta;

    public EmbeddableTypeGenerator(ProcessingEnvironment env,
            TypeElement entityElement, EmbeddableMeta embeddableMeta)
            throws IOException {
        super(env, entityElement, null, null, Constants.METATYPE_PREFIX, "");
        assertNotNull(embeddableMeta);
        this.embeddableMeta = embeddableMeta;
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
        iprint("/** */%n");
        printGenerated();
        iprint("public final class %1$s implements %2$s<%3$s> {%n",
        /* 1 */simpleName,
        /* 2 */EmbeddableType.class.getName(),
        /* 3 */embeddableMeta.getEmbeddableElement().getQualifiedName());
        print("%n");
        indent();
        printValidateVersionStaticInitializer();
        printFields();
        printMethods();
        unindent();
        iprint("}%n");
    }

    protected void printFields() {
        printSingletonField();
    }

    protected void printSingletonField() {
        iprint("private static final %1$s __singleton = new %1$s();%n",
                simpleName);
        print("%n");
    }

    protected void printMethods() {
        printGetEmbeddablePropertyTypesMethod();
        printNewEmbeddableMethod();
        printGetSingletonInternalMethod();
    }

    protected void printGetEmbeddablePropertyTypesMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s<%2$s<ENTITY, ?>> getEmbeddablePropertyTypes(String embeddedPropertyName, Class<ENTITY> entityClass, %3$s namingType) {%n",
                List.class.getName(), EntityPropertyType.class.getName(),
                NamingType.class.getName());
        iprint("    return %1$s.asList(%n", Arrays.class.getName());
        for (Iterator<EmbeddablePropertyMeta> it = embeddableMeta
                .getEmbeddablePropertyMetas().iterator(); it.hasNext();) {
            EmbeddablePropertyMeta pm = it.next();
            EmbeddablePropertyCtTypeVisitor visitor = new EmbeddablePropertyCtTypeVisitor();
            pm.getCtType().accept(visitor, null);
            BasicCtType basicCtType = visitor.basicCtType;
            WrapperCtType wrapperCtType = visitor.wrapperCtType;
            HolderCtType holderCtType = visitor.holderCtType;

            String newWrapperExpr;
            if (basicCtType.isEnum()) {
                newWrapperExpr = String.format("new %s(%s.class)",
                        wrapperCtType.getTypeName(),
                        basicCtType.getBoxedTypeName());
            } else {
                newWrapperExpr = String.format("new %s()",
                        wrapperCtType.getTypeName());
            }
            String parentEntityPropertyType = "null";
            String parentEntityBoxedTypeName = Object.class.getName();
            String holderType = "null";
            String holderTypeName = "Object";
            if (holderCtType != null) {
                holderType = holderCtType.getInstantiationCommand();
                holderTypeName = holderCtType.getTypeName();
            }
            iprint("        new %1$s<Object, ENTITY, %3$s, %16$s>(entityClass, %15$s.class, %3$s.class, () -> %9$s, null, %10$s, embeddedPropertyName + \".%4$s\", \"%5$s\", namingType, %6$s, %7$s, %17$s)",
            /* 1 */DefaultPropertyType.class.getName(),
            /* 2 */null,
            /* 3 */basicCtType.getBoxedTypeName(),
            /* 4 */pm.getName(),
            /* 5 */pm.getColumnName(),
            /* 6 */pm.isColumnInsertable(),
            /* 7 */pm.isColumnUpdatable(),
            /* 8 */null,
            /* 9 */newWrapperExpr,
            /* 10 */holderType,
            /* 11 */pm.getBoxedTypeName(),
            /* 12 */parentEntityPropertyType,
            /* 13 */parentEntityBoxedTypeName,
            /* 14 */null,
            /* 15 */pm.getBoxedClassName(),
            /* 16 */holderTypeName,
            /* 17 */pm.isColumnQuoteRequired());
            print(it.hasNext() ? ",%n" : "");
        }
        print(");%n");
        iprint("}%n");
        print("%n");
    }

    protected void printNewEmbeddableMethod() {
        iprint("@Override%n");
        iprint("public <ENTITY> %1$s newEmbeddable(String embeddedPropertyName, %2$s<String, %3$s<ENTITY, ?>> __args) {%n",
                embeddableMeta.getEmbeddableElement().getQualifiedName(),
                Map.class.getName(), Property.class.getName());
        if (embeddableMeta.isAbstract()) {
            iprint("    return null;%n");
        } else {
            iprint("    return new %1$s(%n", embeddableMeta
                    .getEmbeddableElement().getQualifiedName());
            for (Iterator<EmbeddablePropertyMeta> it = embeddableMeta
                    .getEmbeddablePropertyMetas().iterator(); it.hasNext();) {
                EmbeddablePropertyMeta propertyMeta = it.next();
                iprint("        (%1$s)(__args.get(embeddedPropertyName + \".%2$s\") != null ? __args.get(embeddedPropertyName + \".%2$s\").get() : null)",
                        TypeMirrorUtil.boxIfPrimitive(propertyMeta.getType(),
                                env), propertyMeta.getName());
                if (it.hasNext()) {
                    print(",%n");
                }
            }
            print(");%n");
        }
        iprint("}%n");
        print("%n");
    }

    protected void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        iprint("public static %1$s getSingletonInternal() {%n", simpleName);
        iprint("    return __singleton;%n");
        iprint("}%n");
        print("%n");
    }

    protected class EmbeddablePropertyCtTypeVisitor extends
            SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected BasicCtType basicCtType;

        protected WrapperCtType wrapperCtType;

        protected HolderCtType holderCtType;

        @Override
        protected Void defaultAction(CtType ctType, Void p)
                throws RuntimeException {
            assertNotNull(basicCtType);
            assertNotNull(wrapperCtType);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
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
        public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType,
                Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
        }

        @Override
        public Void visitBasicCtType(BasicCtType basicCtType, Void p)
                throws RuntimeException {
            this.basicCtType = basicCtType;
            this.wrapperCtType = basicCtType.getWrapperCtType();
            return defaultAction(basicCtType, p);
        }

        @Override
        public Void visitHolderCtType(HolderCtType holderCtType, Void p)
                throws RuntimeException {
            this.holderCtType = holderCtType;
            return visitBasicCtType(holderCtType.getBasicCtType(), p);
        }
    }

}
