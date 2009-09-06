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

import org.seasar.doma.domain.BuiltinBigDecimalDomain;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.jdbc.entity.AssignedIdProperty;
import org.seasar.doma.jdbc.entity.BasicProperty;
import org.seasar.doma.jdbc.entity.BuiltinEntityListener;
import org.seasar.doma.jdbc.entity.DomaAbstractEntity;
import org.seasar.doma.jdbc.entity.EntityProperty;
import org.seasar.doma.jdbc.entity.GeneratedIdProperty;
import org.seasar.doma.jdbc.entity.VersionProperty;

@Generated("")
public class Emp_ extends DomaAbstractEntity<Emp> implements Emp, Serializable {

    private static final long serialVersionUID = 1L;

    private static final BuiltinEntityListener __listener = new BuiltinEntityListener();

    private transient final AssignedIdProperty<BuiltinIntegerDomain> id = new AssignedIdProperty<BuiltinIntegerDomain>(
            "id", null, new BuiltinIntegerDomain());

    private transient final BasicProperty<BuiltinStringDomain> name = new BasicProperty<BuiltinStringDomain>(
            "name", null, new BuiltinStringDomain(), true, true);

    private transient final BasicProperty<BuiltinBigDecimalDomain> salary = new BasicProperty<BuiltinBigDecimalDomain>(
            "salary", null, new BuiltinBigDecimalDomain(), true, true);

    private transient final VersionProperty<BuiltinIntegerDomain> version = new VersionProperty<BuiltinIntegerDomain>(
            "version", null, new BuiltinIntegerDomain());

    private final String __name = "emp";

    private transient List<EntityProperty<?>> __properties;

    private transient Map<String, EntityProperty<?>> __propertyMap;

    public Emp_() {
        super(null, null, null);
    }

    @Override
    public BuiltinIntegerDomain id() {
        return id.getDomain();
    }

    @Override
    public BuiltinStringDomain name() {
        return name.getDomain();
    }

    @Override
    public BuiltinBigDecimalDomain salary() {
        return salary.getDomain();
    }

    @Override
    public BuiltinIntegerDomain version() {
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
    public List<EntityProperty<?>> __getEntityProperties() {
        if (__properties == null) {
            List<EntityProperty<?>> list = new ArrayList<EntityProperty<?>>();
            list.add(id);
            list.add(name);
            list.add(salary);
            list.add(version);
            __properties = Collections.unmodifiableList(list);
        }
        return __properties;
    }

    @Override
    public EntityProperty<?> __getEntityProperty(String propertyName) {
        if (__propertyMap == null) {
            Map<String, EntityProperty<?>> map = new HashMap<String, EntityProperty<?>>();
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
