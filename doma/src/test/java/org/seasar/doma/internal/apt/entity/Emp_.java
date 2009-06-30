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
package org.seasar.doma.internal.apt.entity;

import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.DomaAbstractEntity;
import org.seasar.doma.internal.jdbc.GeneratedIdProperty;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.VersionProperty;


/**
 * @author taedium
 * 
 */
public class Emp_ extends DomaAbstractEntity<Emp> implements Emp {

    public Emp_() {
        super(null, null, null);
    }

    @Override
    public IntegerDomain id() {
        return null;
    }

    @Override
    public StringDomain name() {
        return null;
    }

    @Override
    public BigDecimalDomain salary() {
        return null;
    }

    @Override
    public StringDomain temp() {
        return null;
    }

    @Override
    public IntegerDomain version() {
        return null;
    }

    @Override
    public Emp __asInterface() {
        return null;
    }

    @Override
    public String __getName() {
        return null;
    }

    @Override
    public Property<?> __getPropertyByName(String propertyName) {
        return null;
    }

    @Override
    public List<Property<?>> __getProperties() {
        return null;
    }

    @Override
    public GeneratedIdProperty<?> __getGeneratedIdProperty() {
        return null;
    }

    @Override
    public VersionProperty<?> __getVersionProperty() {
        return null;
    }

    @Override
    public void __preDelete() {
    }

    @Override
    public void __preInsert() {
    }

    @Override
    public void __preUpdate() {
    }

}
