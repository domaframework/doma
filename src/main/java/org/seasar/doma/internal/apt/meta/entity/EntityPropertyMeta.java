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
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.meta.id.IdGeneratorMeta;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta extends AbstractPropertyMeta {

    private final Context ctx;

    private final String fieldPrefix;

    private boolean id;

    private boolean version;

    private IdGeneratorMeta idGeneratorMeta;

    public EntityPropertyMeta(Context ctx, VariableElement fieldElement,
            String name, CtType ctType, ColumnReflection columnReflection,
            String fieldPrefix) {
        super(fieldElement, name, ctType, columnReflection);
        assertNotNull(ctx, fieldPrefix);
        this.ctx = ctx;
        this.fieldPrefix = fieldPrefix;
    }

    public String getFieldName() {
        return fieldPrefix + name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public IdGeneratorMeta getIdGeneratorMeta() {
        return idGeneratorMeta;
    }

    public void setIdGeneratorMeta(IdGeneratorMeta idGeneratorMeta) {
        this.idGeneratorMeta = idGeneratorMeta;
    }

    public boolean isEmbedded() {
        return ctType
                .accept(new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {
                    @Override
                    public Boolean visitEmbeddableCtType(
                            EmbeddableCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }
                }, null);
    }

    public String getEmbeddableDescTypeName() {
        TypeElement typeElement = ctType.getTypeElement();
        if (typeElement == null) {
            throw new AptIllegalStateException("typeElement must not be null.");
        }
        return ctx.getMetas().toFullDescName(typeElement);
    }
}
