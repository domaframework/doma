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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.entity.Emp;

/** @author nakamura-to */
@Dao(config = MyConfig.class)
public interface EntityResultDao {

  @Select
  Emp selectSingleResult();

  @Select
  Optional<Emp> selectOptionalSingleResult();

  @Select
  Optional<Emp> selectOptionalSingleResultWithExpansion();

  @Select
  List<Emp> selectResultList();

  @Select(strategy = SelectType.STREAM)
  <R> R stream(Function<Stream<Emp>, R> mapper);
}
