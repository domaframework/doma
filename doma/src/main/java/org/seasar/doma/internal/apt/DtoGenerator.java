/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityPropertyMeta;
import org.seasar.doma.internal.util.StringUtil;

/**
 * 
 * @author taedium
 * 
 */
public class DtoGenerator extends AbstractGenerator {

    protected final EntityMeta entityMeta;

    public DtoGenerator(ProcessingEnvironment env, TypeElement entityElement,
            EntityMeta entityMeta) throws IOException {
        super(env, entityElement, createQualifiedName(env, entityElement));
        assertNotNull(entityMeta);
        this.entityMeta = entityMeta;
    }

    protected static String createQualifiedName(ProcessingEnvironment env,
            TypeElement typeElement) {
        assertNotNull(typeElement);
        String base = Options.getDtoPackage(env);
        if (base == null) {
            PackageElement packageElement = env.getElementUtils().getPackageOf(
                    typeElement);
            base = packageElement.getQualifiedName().toString();
        }
        if (!base.isEmpty()) {
            base += ".";
        }
        return base + typeElement.getSimpleName() + Options.getDtoSuffix(env);
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
        iprint("//");
        printGenerated();
        iprint("public class %1$s implements %2$s {%n", simpleName,
                Serializable.class.getName());
        print("%n");
        indent();
        printFields();
        printMethods();
        unindent();
        iprint("}%n");
    }

    protected void printFields() {
        printSerialVersionUIDField();
        printPropertyField();
    }

    protected void printSerialVersionUIDField() {
        iprint("private static final long serialVersionUID = %1$sL;%n",
                entityMeta.getSerialVersionUID());
        print("%n");
    }

    protected void printPropertyField() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isListReturnType()) {
                iprint("private %1$s<%2$s> %3$s = new %4$s<%2$s>();%n",
                        List.class.getName(), pm.getDomainValueTypeName(), pm
                                .getName(), ArrayList.class.getName());
            } else {
                iprint("private %1$s %2$s;%n", pm.getDomainValueTypeName(), pm
                        .getName());
            }
            print("%n");
        }
    }

    protected void printMethods() {
        printPropertyMethods();
    }

    protected void printPropertyMethods() {
        for (EntityPropertyMeta pm : entityMeta.getAllPropertyMetas()) {
            if (pm.isListReturnType()) {
                iprint("public %1$s<%2$s> get%3$s() {%n", List.class.getName(),
                        pm.getDomainValueTypeName(), StringUtil.capitalize(pm
                                .getName()));
                iprint("    return %1$s;%n", pm.getName());
                iprint("}%n");
                print("%n");
                iprint("public void set%1$s(%2$s<%3$s> %4$s) {%n", StringUtil
                        .capitalize(pm.getName()), List.class.getName(), pm
                        .getDomainValueTypeName(), pm.getName());
                iprint("    this.%1$s = %1$s;%n", pm.getName());
                iprint("}%n");
                print("%n");
            } else {
                iprint("public %1$s get%2$s() {%n",
                        pm.getDomainValueTypeName(), StringUtil.capitalize(pm
                                .getName()));
                iprint("    return %1$s;%n", pm.getName());
                iprint("}%n");
                print("%n");
                iprint("public void set%1$s(%2$s %3$s) {%n", StringUtil
                        .capitalize(pm.getName()), pm.getDomainValueTypeName(),
                        pm.getName());
                iprint("    this.%1$s = %1$s;%n", pm.getName());
                iprint("}%n");
                print("%n");
            }
        }
    }
}
