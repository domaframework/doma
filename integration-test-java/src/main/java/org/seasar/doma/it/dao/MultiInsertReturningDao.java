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
import org.seasar.doma.Dao;
import org.seasar.doma.MultiInsert;
import org.seasar.doma.Returning;
import org.seasar.doma.it.entity.Address;

@Dao
public interface MultiInsertReturningDao {

  @MultiInsert(returning = @Returning)
  List<Address> insertThenReturnAll(List<Address> addresses);

  @MultiInsert(returning = @Returning(include = "addressId"))
  List<Address> insertThenReturnOnlyId(List<Address> addresses);

  @MultiInsert(returning = @Returning(exclude = "version"))
  List<Address> insertThenReturnExceptVersion(List<Address> addresses);
}
