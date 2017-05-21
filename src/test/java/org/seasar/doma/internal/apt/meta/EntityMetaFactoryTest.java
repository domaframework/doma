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
package org.seasar.doma.internal.apt.meta;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.entity.NamingType1Entity;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityMetaFactoryTest extends AptTestCase {

    public void testNaming1Type() throws Exception {
        Class<?> target = NamingType1Entity.class;
        addCompilationUnit(target);
        addProcessor(new AptProcessor(env -> {
            EntityMeta entityMeta = createEntityMeta(env, target);
            assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
        }));
        compile();
        assertTrue(getCompiledResult());
    }

    public void testNaming2Type() throws Exception {
        Class<?> target = NamingType2Entity.class;
        addCompilationUnit(target);
        addProcessor(new AptProcessor(env -> {
            EntityMeta entityMeta = createEntityMeta(env, target);
            assertEquals(NamingType.UPPER_CASE, entityMeta.getNamingType());
        }));
        compile();
        assertTrue(getCompiledResult());
    }

    public void testNaming3Type() throws Exception {
        Class<?> target = NamingType3Entity.class;
        addCompilationUnit(target);
        addProcessor(new AptProcessor(env -> {
            EntityMeta entityMeta = createEntityMeta(env, target);
            assertEquals(NamingType.NONE, entityMeta.getNamingType());
        }));
        compile();
        assertTrue(getCompiledResult());
    }

    protected EntityMeta createEntityMeta(ProcessingEnvironment env,
            Class<?> clazz) {
        EntityPropertyMetaFactory propertyMetaFactory = new EntityPropertyMetaFactory(
                env);
        EntityMetaFactory entityMetaFactory = new EntityMetaFactory(env,
                propertyMetaFactory);
        TypeElement typeElement = createTypeElement(env, clazz);
        return entityMetaFactory
                .createTypeElementMeta(typeElement);
    }
}
