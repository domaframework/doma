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
package org.seasar.doma.internal.apt.processor.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;

/**
 * @author nakamura-to
 * 
 */
@Dao(config = MyConfig.class)
public interface MapResultDao {

    @Select
    Map<String, Object> selectSingleResult();

    @Select
    Optional<Map<String, Object>> selectOptionalSingleResult();

    @Select
    List<Map<String, Object>> selectResultList();

    @Select(strategy = SelectType.STREAM)
    <R> R stream(Function<Stream<Map<String, Object>>, R> mapper);

}
