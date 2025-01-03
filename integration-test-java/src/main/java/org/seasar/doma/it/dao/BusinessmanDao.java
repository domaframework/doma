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
package org.seasar.doma.it.dao;

import java.util.List;
import java.util.OptionalInt;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.Businessman;

@Dao
public interface BusinessmanDao {

  @Select
  List<Businessman> selectAll();

  @Select
  Businessman selectById(OptionalInt id);

  @Select
  List<Businessman> selectByExample(Businessman businessman);

  @Insert
  int insert(Businessman entity);

  @Update
  int update(Businessman entity);

  @Delete
  int delete(Businessman entity);

  @BatchInsert
  int[] insert(List<Businessman> entity);

  @BatchUpdate
  int[] update(List<Businessman> entity);

  @BatchDelete
  int[] delete(List<Businessman> entity);

  @MultiInsert
  int insertMultiRows(List<Businessman> entities);
}
