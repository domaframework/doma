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
import org.seasar.doma.it.entity.IdentityStrategy2;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface IdentityStrategy2Dao {

  @Select
  List<IdentityStrategy2> selectAll();

  @Insert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOrIgnore(IdentityStrategy2 entity);

  @Insert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int insertOrUpdate(IdentityStrategy2 entity);

  @BatchInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int[] insertOrIgnore(List<IdentityStrategy2> entities);

  @BatchInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int[] insertOrUpdate(List<IdentityStrategy2> entities);

  @MultiInsert(duplicateKeyType = DuplicateKeyType.IGNORE)
  int insertOrIgnoreMultiRows(List<IdentityStrategy2> entities);

  @MultiInsert(
      duplicateKeyType = DuplicateKeyType.UPDATE,
      duplicateKeys = {"uniqueValue"})
  int insertOrUpdateMultiRows(List<IdentityStrategy2> entities);
}
