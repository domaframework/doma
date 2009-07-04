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
package example.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.AssignedIdProperty;
import org.seasar.doma.internal.jdbc.BasicProperty;
import org.seasar.doma.internal.jdbc.DomaAbstractEntity;
import org.seasar.doma.internal.jdbc.GeneratedIdProperty;
import org.seasar.doma.internal.jdbc.NullEntityListener;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.VersionProperty;

@Generated("")
public class Emp_ extends DomaAbstractEntity<Emp> implements Emp, Serializable {

    private static final long serialVersionUID = 1L;

    private static final NullEntityListener __listener = new NullEntityListener();

    private transient AssignedIdProperty<IntegerDomain> id = new AssignedIdProperty<IntegerDomain>(
            "id", null, new IntegerDomain(), true, true);

    private transient BasicProperty<StringDomain> name = new BasicProperty<StringDomain>(
            "name", null, new StringDomain(), true, true);

    private transient BasicProperty<BigDecimalDomain> salary = new BasicProperty<BigDecimalDomain>(
            "salary", null, new BigDecimalDomain(), true, true);

    private transient VersionProperty<IntegerDomain> version = new VersionProperty<IntegerDomain>(
            "version", null, new IntegerDomain(), true, true);

    private final String __name = "emp";

    private transient List<Property<?>> __properties;

    private transient Map<String, Property<?>> __propertyMap;

    public Emp_() {
        super(null, null, null);
    }

    @Override
    public IntegerDomain id() {
        return id.getDomain();
    }

    @Override
    public StringDomain name() {
        return name.getDomain();
    }

    @Override
    public BigDecimalDomain salary() {
        return salary.getDomain();
    }

    @Override
    public IntegerDomain version() {
        return version.getDomain();
    }

    @Override
    public Emp __asInterface() {
        return this;
    }

    @Override
    public String __getName() {
        return __name;
    }

    @Override
    public List<Property<?>> __getProperties() {
        if (__properties == null) {
            List<Property<?>> list = new ArrayList<Property<?>>();
            list.add(id);
            list.add(name);
            list.add(salary);
            list.add(version);
            __properties = Collections.unmodifiableList(list);
        }
        return __properties;
    }

    @Override
    public Property<?> __getPropertyByName(String propertyName) {
        if (__propertyMap == null) {
            Map<String, Property<?>> map = new HashMap<String, Property<?>>();
            map.put("id", id);
            map.put("name", name);
            map.put("salary", salary);
            map.put("version", version);
            __propertyMap = Collections.unmodifiableMap(map);
        }
        return __propertyMap.get(propertyName);
    }

    @Override
    public GeneratedIdProperty<?> __getGeneratedIdProperty() {
        return null;
    }

    @Override
    public VersionProperty<?> __getVersionProperty() {
        return version;
    }

    @Override
    public void __preInsert() {
        __listener.preInsert(this);
    }

    @Override
    public void __preUpdate() {
        __listener.preUpdate(this);
    }

    @Override
    public void __preDelete() {
        __listener.preDelete(this);
    }

}
