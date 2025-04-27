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

import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Returning;
import org.seasar.doma.Update;
import org.seasar.doma.internal.apt.processor.entity.Emp;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

@Dao
public interface ReturningDao {

  @Delete(returning = @Returning)
  Emp deleteThenReturnAll(Emp employee);

  @Delete(returning = @Returning(include = "id"))
  Emp deleteThenReturnOnlyId(Emp employee);

  @Delete(returning = @Returning(include = {"id", "name"}))
  Emp deleteThenReturnOnlyIdAndName(Emp employee);

  @Delete(returning = @Returning(exclude = "version"))
  Emp deleteThenReturnExceptVersion(Emp employee);

  @Insert(returning = @Returning)
  Emp insertThenReturnAll(Emp employee);

  @Insert(returning = @Returning, duplicateKeyType = DuplicateKeyType.IGNORE)
  Optional<Emp> insertOrIgnoreThenReturnAll(Emp employee);

  @Insert(returning = @Returning(include = "id"))
  Emp insertThenReturnOnlyId(Emp employee);

  @Insert(returning = @Returning(exclude = "version"))
  Emp insertThenReturnExceptVersion(Emp employee);

  @Update(returning = @Returning)
  Emp updateThenReturnAll(Emp employee);

  @Update(returning = @Returning(include = "id"))
  Emp updateThenReturnOnlyId(Emp employee);

  @Update(returning = @Returning(exclude = "version"))
  Emp updateThenReturnExceptVersion(Emp employee);
}
