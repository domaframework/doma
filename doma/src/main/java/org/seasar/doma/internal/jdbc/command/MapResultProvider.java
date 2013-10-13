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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.query.Query;

/***
 * 
 * @author nakamura-to
 * 
 * @param <MAP>
 * @param <CONTAINER>
 */
public class MapResultProvider<CONTAINER> implements ResultProvider<CONTAINER> {

    protected final MapFetcher fetcher;

    protected final Function<Map<String, Object>, CONTAINER> mapper;

    /**
     * 
     * @param query
     * @param keyNamingType
     * @param mapper
     */
    public MapResultProvider(Query query, MapKeyNamingType keyNamingType,
            Function<Map<String, Object>, CONTAINER> mapper) {
        assertNotNull(query, keyNamingType);
        this.fetcher = new MapFetcher(query, keyNamingType);
        this.mapper = mapper;
    }

    @Override
    public CONTAINER get(ResultSet resultSet) throws SQLException {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        fetcher.fetch(resultSet, map);
        return mapper.apply(map);
    }

    @Override
    public CONTAINER getDefault() {
        return mapper.apply(null);
    }

}
