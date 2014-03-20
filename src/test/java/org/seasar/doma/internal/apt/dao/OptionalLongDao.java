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
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.Dao;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.Select;
import org.seasar.doma.SelectStrategyType;
import org.seasar.doma.jdbc.Reference;

import example.entity.Emp;

/**
 * @author nakamura-to
 *
 */
@Dao(config = MyConfig.class)
public interface OptionalLongDao {

    @Select
    Emp selectById(OptionalLong id);

    @Select
    OptionalLong selectAgeById(OptionalLong id);

    @Select
    List<OptionalLong> selectAllAge();

    @Select(strategy = SelectStrategyType.STREAM)
    <R> R selectAllAge(Function<Stream<OptionalLong>, R> mapper);

    @org.seasar.doma.Function
    OptionalLong getSingleResult(@In OptionalLong in,
            @InOut Reference<OptionalLong> inout,
            @Out Reference<OptionalLong> out,
            @ResultSet List<OptionalLong> resultSet);

    @org.seasar.doma.Function
    List<OptionalLong> getResultList();

    @Procedure
    void execute(@In OptionalLong in, @InOut Reference<OptionalLong> inout,
            @Out Reference<OptionalLong> out,
            @ResultSet List<OptionalLong> resultSet);

}
