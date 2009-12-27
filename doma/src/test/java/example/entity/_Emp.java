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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.seasar.doma.internal.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _Emp implements EntityType<Emp> {

    private static _Emp singleton = new _Emp();

    private static final NullEntityListener __listener = new NullEntityListener();

    public static final AssignedIdPropertyType<Emp, Integer> id = new AssignedIdPropertyType<Emp, Integer>(
            "id", "ID") {

        @Override
        public IntegerWrapper getWrapper(Emp entity) {
            return new Wrapper(entity);
        }

        class Wrapper extends IntegerWrapper {

            private final Emp entity;

            Wrapper(Emp entity) {
                this.entity = entity;
            }

            @Override
            public Integer get() {
                if (entity == null) {
                    return null;
                }
                return entity.id;
            }

            @Override
            public void set(Integer value) {
                if (entity == null) {
                    return;
                }
                entity.id = value;
            }
        }
    };

    private static final BasicPropertyType<Emp, String> name = new BasicPropertyType<Emp, String>(
            "name", "NAME", true, true) {
        @Override
        public StringWrapper getWrapper(Emp entity) {
            return new Wrapper(entity);
        }

        class Wrapper extends StringWrapper {

            private final Emp entity;

            Wrapper(Emp entity) {
                this.entity = entity;
            }

            @Override
            public String get() {
                if (entity == null) {
                    return null;
                }
                return entity.name;
            }

            @Override
            public void set(String value) {
                if (entity == null) {
                    return;
                }
                entity.name = value;
            }
        }
    };

    private static final BasicPropertyType<Emp, BigDecimal> salary = new BasicPropertyType<Emp, BigDecimal>(
            "salary", "SALARY", true, true) {
        @Override
        public BigDecimalWrapper getWrapper(Emp entity) {
            return new Wrapper(entity);
        }

        class Wrapper extends BigDecimalWrapper {

            private final Emp entity;

            Wrapper(Emp entity) {
                this.entity = entity;
            }

            @Override
            public BigDecimal get() {
                if (entity == null) {
                    return null;
                }
                return entity.salary;
            }

            @Override
            public void set(BigDecimal value) {
                if (entity == null) {
                    return;
                }
                entity.salary = value;
            }
        }

    };

    private static final VersionPropertyType<Emp, Integer> version = new VersionPropertyType<Emp, Integer>(
            "version", "VERSION") {
        @Override
        public IntegerWrapper getWrapper(Emp entity) {
            return new Wrapper(entity);
        }

        class Wrapper extends IntegerWrapper {

            private final Emp entity;

            Wrapper(Emp entity) {
                this.entity = entity;
            }

            @Override
            public Integer get() {
                if (entity == null) {
                    return null;
                }
                return entity.version;
            }

            @Override
            public void set(Integer value) {
                if (entity == null) {
                    return;
                }
                entity.version = value;
            }
        }
    };

    private static final String __name = "Emp";

    private static final String __catalogName = null;

    private static final String __schemaName = null;

    private static final String __tableName = "EMP";

    private static final List<EntityPropertyType<Emp, ?>> __propertyTypes;
    static {
        List<EntityPropertyType<Emp, ?>> list = new ArrayList<EntityPropertyType<Emp, ?>>();
        list.add(id);
        list.add(name);
        list.add(salary);
        list.add(version);
        __propertyTypes = Collections.unmodifiableList(list);
    }

    private static final Map<String, EntityPropertyType<Emp, ?>> __propertyTypeMap;
    static {
        Map<String, EntityPropertyType<Emp, ?>> map = new HashMap<String, EntityPropertyType<Emp, ?>>();
        map.put("id", id);
        map.put("name", name);
        map.put("salary", salary);
        map.put("version", version);
        __propertyTypeMap = Collections.unmodifiableMap(map);
    }

    @Override
    public Emp newEntity() {
        return new Emp();
    }

    @Override
    public Class<Emp> getEntityClass() {
        return Emp.class;
    }

    @Override
    public String getName() {
        return __name;
    }

    @Override
    public List<EntityPropertyType<Emp, ?>> getEntityPropertyTypes() {
        return __propertyTypes;
    }

    @Override
    public EntityPropertyType<Emp, ?> getEntityPropertyType(String propertyName) {
        return __propertyTypeMap.get(propertyName);
    }

    @Override
    public void saveCurrentStates(Emp entity) {
        Emp __currentStates = new Emp();
        id.getWrapper(__currentStates).set(id.getWrapper(entity).getCopy());
        name.getWrapper(__currentStates).set(name.getWrapper(entity).getCopy());
        salary.getWrapper(__currentStates).set(
                salary.getWrapper(entity).getCopy());
        version.getWrapper(__currentStates).set(
                version.getWrapper(entity).getCopy());
        entity.originalStates = __currentStates;
    }

    @Override
    public Emp getOriginalStates(Emp entity) {
        if (entity.originalStates instanceof Emp) {
            Emp originalStates = (Emp) entity.originalStates;
            return originalStates;
        }
        return null;
    }

    @Override
    public GeneratedIdPropertyType<Emp, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public VersionPropertyType<Emp, ?> getVersionPropertyType() {
        return version;
    }

    @Override
    public void preInsert(Emp entity) {
        __listener.preInsert(entity);
    }

    @Override
    public void preUpdate(Emp entity) {
        __listener.preUpdate(entity);
    }

    @Override
    public void preDelete(Emp entity) {
        __listener.preDelete(entity);
    }

    @Override
    public String getCatalogName() {
        return __catalogName;
    }

    @Override
    public String getSchemaName() {
        return __schemaName;
    }

    @Override
    public String getTableName() {
        return __tableName;
    }

    public static _Emp get() {
        return singleton;
    }
}
