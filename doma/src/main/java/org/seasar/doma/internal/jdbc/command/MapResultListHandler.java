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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 * 
 */
public class MapResultListHandler implements
        ResultSetHandler<List<Map<String, Object>>> {

    private final MapKeyNamingType mapKeyNamingType;

    public MapResultListHandler(MapKeyNamingType mapKeyNamingType) {
        assertNotNull(mapKeyNamingType);
        this.mapKeyNamingType = mapKeyNamingType;
    }

    @Override
    public List<Map<String, Object>> handle(ResultSet resultSet,
            SelectQuery query) throws SQLException {
        assertNotNull(resultSet, query);
        MapFetcher fetcher = new MapFetcher(query, mapKeyNamingType);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        while (resultSet.next()) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            fetcher.fetch(resultSet, map);
            mapList.add(map);
        }
        if (query.isResultEnsured() && mapList.isEmpty()) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig()
                    .getExceptionSqlLogType(), sql);
        }
        return mapList;
    }

}
