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

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityMetaFactory;
import org.seasar.doma.internal.apt.meta.EntityPropertyMetaFactory;

/**
 * @author taedium
 * 
 */
@SupportedAnnotationTypes({ "org.seasar.doma.Entity" })
@SupportedOptions({ Options.ENTITY_FIELD_PREFIX, Options.DOMAIN_CONVERTERS,
        Options.VERSION_VALIDATION, Options.TEST, Options.DEBUG })
public class EntityProcessor extends AbstractGeneratingProcessor<EntityMeta> {

    @Override
    protected EntityMetaFactory createTypeElementMetaFactory() {
        EntityPropertyMetaFactory propertyMetaFactory = createEntityPropertyMetaFactory();
        return new EntityMetaFactory(processingEnv, propertyMetaFactory);
    }

    protected EntityPropertyMetaFactory createEntityPropertyMetaFactory() {
        return new EntityPropertyMetaFactory(processingEnv);
    }

    @Override
    protected Generator createGenerator(TypeElement typeElement, EntityMeta meta)
            throws IOException {
        assertNotNull(typeElement, meta);
        return new EntityTypeGenerator(processingEnv, typeElement, meta);
    }

}
