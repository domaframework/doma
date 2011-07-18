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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import java.util.Map;

import org.seasar.doma.MapKeyNamingType;

/**
 * @author taedium
 * 
 */
public class MapListParameter implements
        ListParameter<Object, Map<String, Object>> {

    protected final MapKeyNamingType mapKeyNamingType;

    protected final List<Map<String, Object>> mapList;

    protected final String name;

    public MapListParameter(MapKeyNamingType mapKeyNamingType,
            List<Map<String, Object>> mapList, String name) {
        assertNotNull(mapKeyNamingType, mapList, name);
        this.mapKeyNamingType = mapKeyNamingType;
        this.mapList = mapList;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return mapList;
    }

    public MapKeyNamingType getMapKeyNamingType() {
        return mapKeyNamingType;
    }

    @Override
    public void add(Map<String, Object> map) {
        mapList.add(map);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitMapListParameter(this, p);
    }

}
