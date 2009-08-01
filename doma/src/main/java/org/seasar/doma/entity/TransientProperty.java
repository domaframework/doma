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
package org.seasar.doma.entity;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;

/**
 * @author taedium
 * 
 */
public class TransientProperty<D extends Domain<?, ?>> implements
        EntityProperty<D> {

    protected final String name;

    protected final D domain;

    public TransientProperty(String name, D domain) {
        if (name == null) {
            throw new DomaNullPointerException("name");
        }
        if (domain == null) {
            throw new DomaNullPointerException("domain");
        }
        this.name = name;
        this.domain = domain;
    }

    @Override
    public String getColumnName(Config config) {
        return null;
    }

    @Override
    public D getDomain() {
        return domain;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public boolean isInsertable() {
        return false;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    @Override
    public boolean isVersion() {
        return false;
    }

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public String toString() {
        return domain != null ? domain.toString() : null;
    }
}
