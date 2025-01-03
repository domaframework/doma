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
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Update;
import org.seasar.doma.it.entity.NoId;

@Dao
public interface NoIdDao {

  @Insert
  int insert(NoId entity);

  @Update
  int update(NoId entity);

  @Delete
  int delete(NoId entity);

  @BatchInsert
  int[] insert(List<NoId> entities);

  @BatchUpdate
  int[] update(List<NoId> entities);

  @BatchDelete
  int[] delete(List<NoId> entities);

  @MultiInsert
  int insertMultiRows(List<NoId> entities);
}
