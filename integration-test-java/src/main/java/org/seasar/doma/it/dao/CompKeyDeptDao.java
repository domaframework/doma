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
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.CompKeyDept;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface CompKeyDeptDao {

  @Select
  CompKeyDept selectByIds(Integer departmentId1, Integer departmentId2);

  @Insert(duplicateKeyType = DuplicateKeyType.UPDATE)
  Result<CompKeyDept> insertOnDuplicateKeyUpdate(CompKeyDept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  BatchResult<CompKeyDept> insertOnDuplicateKeyUpdate(List<CompKeyDept> entities);

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  Result<CompKeyDept> insertOnDuplicateKeyIgnore(CompKeyDept entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  BatchResult<CompKeyDept> insertOnDuplicateKeyIgnore(List<CompKeyDept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  MultiResult<CompKeyDept> insertMultiRowsOnDuplicateKeyUpdate(List<CompKeyDept> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  MultiResult<CompKeyDept> insertMultiRowsOnDuplicateKeyIgnore(List<CompKeyDept> entities);
}
