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

import java.lang.reflect.Method;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Config;

/**
 * A context for a pre process of an update.
 * 
 * @param <E>
 *            the entity type
 */
public interface PreUpdateContext<E> {

    /**
     * Whether the entity is changed.
     * <p>
     * This method always returns {@code true}, when {@link Update#sqlFile()} is
     * {@code true}.
     * 
     * @return @code true} if the entity is changed
     */
    public boolean isEntityChanged();

    /**
     * Whether the entity property is changed.
     * <p>
     * This method always returns {@code true}, when {@link Update#sqlFile()} is
     * {@code true}.
     * 
     * @param propertyName
     *            the name of property
     * @return {@code true} if the property is changed
     * @exception EntityPropertyNotDefinedException
     *                if the property is not defined in the entity
     * @see OriginalStates
     */
    public boolean isPropertyChanged(String propertyName);

    /**
     * Returns the entity description.
     * 
     * @return the entity description
     */
    public EntityDesc<?> getEntityDesc();

    /**
     * The method that is annotated with {@link Update}.
     * 
     * @return the method
     */
    public Method getMethod();

    /**
     * Returns the configuration.
     * 
     * @return the configuration
     */
    public Config getConfig();

    /**
     * Returns the new entity.
     * 
     * @return the new entity
     */
    public E getNewEntity();

    /**
     * Sets the new entity.
     * <p>
     * This method is available, when the entity is immutable.
     * 
     * @param newEntity
     *            the entity
     * @throws DomaNullPointerException
     *             if {@code newEntity} is {@code null}
     */
    public void setNewEntity(E newEntity);

}
