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

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.ExternalHolderMeta;
import org.seasar.doma.jdbc.holder.AbstractHolderType;

/**
 * @author taedium
 * 
 */
public class ExternalHolderTypeGenerator extends AbstractGenerator {

    private final ExternalHolderMeta holderMeta;

    private final String holderTypeName;

    private final String simpleMetaClassName;

    private final String typeParamDecl;

    private final boolean parametarized;

    public ExternalHolderTypeGenerator(Context ctx,
            TypeElement typeElement, ExternalHolderMeta holderMeta)
            throws IOException {
        super(ctx, typeElement,
                Constants.EXTERNAL_HOLDER_METATYPE_ROOT_PACKAGE + "."
                        + ctx.getElements().getPackageName(typeElement),
                null,
                Constants.METATYPE_PREFIX, "");
        assertNotNull(holderMeta);
        this.holderMeta = holderMeta;
        this.holderTypeName = ctx.getTypes()
                .getTypeName(holderMeta
                        .getHolderElement().asType());
        this.simpleMetaClassName = ctx.getMetas().toSimpleMetaName(typeElement);
        this.typeParamDecl = makeTypeParamDecl(holderTypeName);
        this.parametarized = !holderMeta.getHolderElement().getTypeParameters()
                .isEmpty();
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

    private void printPackage() {
        if (!packageName.isEmpty()) {
            iprint("package %1$s;%n", packageName);
            iprint("%n");
        }
    }

    private void printClass() {
        if (holderMeta.getHolderElement().getTypeParameters().isEmpty()) {
            iprint("/** */%n");
        } else {
            iprint("/**%n");
            for (TypeParameterElement typeParam : holderMeta.getHolderElement()
                    .getTypeParameters()) {
                iprint(" * @param <%1$s> %1$s%n", typeParam.getSimpleName());
            }
            iprint(" */%n");
        }
        printGenerated();
        iprint("public final class %1$s%5$s extends %2$s<%3$s, %4$s> {%n",
                simpleMetaClassName, AbstractHolderType.class.getName(),
                holderMeta.getValueTypeName(), holderTypeName, typeParamDecl);
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
        if (parametarized) {
            iprint("@SuppressWarnings(\"rawtypes\")%n");
        }
        iprint("private static final %1$s singleton = new %1$s();%n",
                simpleName);
        print("%n");
        iprint("private static final %1$s converter = new %1$s();%n",
                holderMeta.getTypeElement().getQualifiedName());
        print("%n");
    }

    private void printConstructors() {
        iprint("private %1$s() {%n", simpleName);
        BasicCtType basicCtType = holderMeta.getBasicCtType();
        if (basicCtType.isEnum()) {
            iprint("    super(() -> new %1$s(%2$s.class));%n",
                    basicCtType.getWrapperTypeName(),
                    holderMeta.getValueTypeName());
            iprint("}%n");
        } else {
            iprint("    super(() -> new %1$s());%n",
                    basicCtType.getWrapperTypeName());
            iprint("}%n");
        }
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
        if (parametarized) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("protected %1$s newHolder(%2$s value) {%n", holderTypeName,
                holderMeta.getValueTypeName());
        if (parametarized) {
            iprint("    return (%1$s) converter.fromValueToHolder(value);%n",
                    holderTypeName);
        } else {
            iprint("    return converter.fromValueToHolder(value);%n");
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetBasicValueMethod() {
        iprint("@Override%n");
        iprint("protected %1$s getBasicValue(%2$s holder) {%n",
                holderMeta.getValueTypeName(), holderTypeName);
        iprint("    if (holder == null) {%n");
        iprint("        return null;%n");
        iprint("    }%n");
        iprint("    return converter.fromHolderToValue(holder);%n");
        iprint("}%n");
        print("%n");
    }

    private void printGetBasicClassMethod() {
        iprint("@Override%n");
        iprint("public Class<?> getBasicClass() {%n");
        iprint("    return %1$s.class;%n", holderMeta.getValueTypeName());
        iprint("}%n");
        print("%n");
    }

    private void printGetHolderClassMethod() {
        if (parametarized) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
        }
        iprint("@Override%n");
        iprint("public Class<%1$s> getHolderClass() {%n", holderTypeName);
        if (parametarized) {
            iprint("    Class<?> clazz = %1$s.class;%n", holderMeta
                    .getHolderElement().getQualifiedName());
            iprint("    return (Class<%1$s>) clazz;%n", holderTypeName);
        } else {
            iprint("    return %1$s.class;%n", holderMeta.getHolderElement()
                    .getQualifiedName());
        }
        iprint("}%n");
        print("%n");
    }

    private void printGetSingletonInternalMethod() {
        iprint("/**%n");
        iprint(" * @return the singleton%n");
        iprint(" */%n");
        if (parametarized) {
            iprint("@SuppressWarnings(\"unchecked\")%n");
            iprint("public static %1$s %2$s%1$s getSingletonInternal() {%n",
                    typeParamDecl, simpleMetaClassName);
            iprint("    return (%2$s%1$s) singleton;%n", typeParamDecl,
                    simpleMetaClassName);
        } else {
            iprint("public static %1$s getSingletonInternal() {%n",
                    simpleMetaClassName);
            iprint("    return singleton;%n");
        }
        iprint("}%n");
        print("%n");
    }

}
