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
package org.seasar.doma.internal.apt.dao;

import java.util.List;
import java.util.Map;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface ProcedureDao {

    @Procedure(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    void execute(@ResultSet List<Map<String, Object>> result, @In int id);

}
