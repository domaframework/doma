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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;

/**
 * An entity property.
 * 
 * @param <ENTITY>
 *            the entity type
 * @param <BASIC>
 *            the basic type
 */
public interface Property<ENTITY, BASIC> extends JdbcMappable<BASIC> {

    /**
     * Returns the value of this property.
     * 
     * @return the value of this property
     */
    Object get();

    /**
     * Loads the value from the entity to this property.
     * 
     * @param entity
     *            the entity
     * @return this instance
     */
    Property<ENTITY, BASIC> load(ENTITY entity);

    /**
     * Saves the value from this property to the entity.
     * 
     * @param entity
     *            the entity
     * @return this instance
     */
    Property<ENTITY, BASIC> save(ENTITY entity);

    /**
     * Returns this property as {@link InParameter}.
     * 
     * @return the input parameter
     */
    InParameter<BASIC> asInParameter();

}
