/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor.dao;

import java.math.BigDecimal;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.Procedure;
import org.seasar.doma.ResultSet;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.processor.entity.Emp2;
import org.seasar.doma.jdbc.SelectOptions;

@Dao
public interface EnsureResultMappingDao {

  @Select(ensureResultMapping = true)
  Emp2 selectById(Integer id, SelectOptions options);

  @Select(ensureResultMapping = true)
  List<Emp2> selectByNameAndSalary(String name, BigDecimal salary, SelectOptions options);

  @Procedure
  void procedure(@ResultSet(ensureResultMapping = true) List<Emp2> emp);

  @Function(ensureResultMapping = true)
  List<Emp2> function(@ResultSet(ensureResultMapping = true) List<Emp2> emp);
}
